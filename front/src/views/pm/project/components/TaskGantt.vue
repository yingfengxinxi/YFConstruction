<template>
  <div class="task-list-board">
    <el-collapse v-model="collapseValue">
      <div id="gante"></div>
    </el-collapse>
    <!-- 详情 -->
    <task-detail
            v-if="taskDetailShow"
            ref="particulars"
            :id="taskID"
            :detail-index="detailIndex"
            :detail-section="detailSection"
            :no-listener-class="['board-item']"
            @on-handle="detailHandle"
            @close="closeBtn"/>
  </div>
</template>

<script>
import TaskCell from '@/views/taskExamine/task/components/TaskCell'
import TaskMixin from '@/views/taskExamine/task/mixins/TaskMixin'
import BoardMixin from './Board'
import request from '@/utils/request'

export default {
  // 任务列表看板
  name: 'TaskListBoard',
  components: {
    TaskCell
  },
  mixins: [BoardMixin,TaskMixin],
  props: {
    workId: [String, Number],
    permission: {
      type: Object,
      default: () => {
        return {}
      }
    }
  },
  data() {
    return {
      GANTT: null,
      endTime: new Date(),
      showType: 'board',
      loading: false,
      // 新建任务弹出框
      createTaskListShow: false,
      createSubTaskClassId: 'hidden',
      // 新建列表
      taskListName: '',
      // 重命名
      editTaskListName: '',
      // 主数据
      taskList: [],
      // 详情对应的任务对象数据 -- 用于更新数据
      // 详情数据
      taskID: '',
      detailIndex: -1,
      detailSection: -1,
      taskDetailShow: false,

      collapseValue: []
    }
  },
  computed: {},
  watch: {},
  created() {},
  mounted() {},
  updated() {
    //console.log("updated")
    if( this.GANTT != null){
      
      this.getGanttList().then(data =>{
        this.GANTT.init({ganteData: data},false)
      })
    }else{
      
      this.createGantt()
    }

  },
  beforeDestroy() {
  },
  methods: {
    async createGantt(){
      let th_data = {
        title:{value:'任务名称',width:200,showToast:false,shrink:true,sort_type:false,listen_click:true},
        startTime:{value:'开始时间',width:110,showToast:true,time_mode:1,sort_type: 'asc'},
        endTime:{value:'结束时间',width:110,showToast:true,time_mode:2,sort_type: 'asc'},
        mainUser:{value:'负责人',width:60,showToast:false,sort_type:false}
        // ,biaoti:{value:'内容',width:450,shrink:true,showToast:true,edit:true}
      }
      let ganttList = await this.getGanttList()
      this.$gante({
        container:'#gante',
        ganteData: ganttList,
        start_time:ganttList[0].start_time,
        end_time:this.endTime,
        open:false,//子任务是否打开的状态(
        openLoad:false,//是否开启懒加载模式
        height:700,//TODO 动态调整
        time_mode:1,
        th_data:th_data,
        onEdit(data){
          //console.log("onEdit=="+JSON.stringify(data))
        },
        onClick: this.taskDetail,
        onDragChangeTime(data,resolve){
          //console.log("onDragChangeTime=="+JSON.stringify(data))
          resolve(false) // 取消刚才拖动的时间，如果不取消就不用执行
        },
        onLoad(resolve){
          resolve(false,data,start_time,end_time)
        }
      }).then(data =>{
        this.GANTT = data;
      })
    },
    async getGanttList(){
      let ganttList = []
      // console.log("setGanttList=="+JSON.stringify(this.taskList))
      let index = 0;
      for (const taskTab of this.taskList) {
        let sIndex = 0;
        for (const taskInfo of taskTab.list) {
          let taskContent=''
          //优先级
          taskContent += '<span style="background-color:'+ this.priority(taskInfo.priority).color+'" class="priority">'+
                  this.priority(taskInfo.priority).label+'</span>'
          //里程碑
          if(taskInfo.isMilestone==1){
            taskContent += '<i class="wk wk-tag" style="color:#f56c6c;margin-right: 2px"></i>'
          }
          let ganttData = {
            gunter_id:taskInfo.taskId,
            params:{title: taskContent+taskInfo.name, startTime:this.parseTime(taskInfo.startTime,'{y}-{m}-{d}'),
              endTime:this.parseTime(taskInfo.stopTime,'{y}-{m}-{d}'),
              mainUser:taskInfo.mainUser,index:sIndex,section:index},
            start_time:new Date(taskInfo.startTime).getTime(),
            end_time:new Date(taskInfo.stopTime).getTime(),
            bg_color:taskInfo.isEnd == 1 && !taskInfo.checked?'red':taskInfo.checked?"#53D397":null,
            level:1
          }
          if(taskInfo.childAllCount!=null && taskInfo.childAllCount>0){
            ganttData.children = await this.getChildTask(ganttData)
          }
          if(ganttData.end_time>this.endTime){
            this.endTime = ganttData.end_time
          }
          ganttList.push(ganttData)
          sIndex=sIndex+1
        }
        index=index+1
      }
      return ganttList
    },
    async getChildTask(ganttData) {
      let childTaskList = []
      let res = await request({
        url: 'workTask/queryTaskInfo/'+ganttData.gunter_id,
        method: 'post'
      })
      for (const childTask of res.data.childTask) {
        // console.log("childTask=="+JSON.stringify(childTask))
        let child = {
          gunter_id:childTask.taskId,
          params:{title:childTask.name, startTime:ganttData.params.startTime,endTime:ganttData.params.endTime,
            mainUser:childTask.mainUser},
          start_time:ganttData.start_time,
          end_time:ganttData.end_time,
          bg_color:childTask.isEnd == 1 && !childTask.checked?'red':childTask.status==5?"#53D397":null,
          level:2
        }
        if(childTask.stopTime){
          child.params.endTime = this.parseTime(childTask.stopTime,'{y}-{m}-{d}')
          child.end_time = new Date(childTask.stopTime).getTime()
          if(child.end_time>this.endTime){
            this.endTime = child.end_time
          }
        }
        childTaskList.push(child)
      }
      // console.log("childTaskList=="+JSON.stringify(childTaskList))
      return childTaskList
    },
    priority(value) {
      if (value == 0 || !value) {
        return this.priorityList[3] // 默认读取 priorityList 返回
      }
      return this.getPriorityColor(value)
    },
    /**
     * 全部展开
     */
    getListRest() {
      this.collapseValue = this.taskList.map((item, index) => {
        return index
      })
    },
    taskDetail(data){
      this.taskID = data.data.gunter_id
      this.detailIndex = data.data.params.index
      this.detailSection = data.data.params.section
      this.taskDetailShow = true
    },
    /**
     * 任务cell 操作
     */
    taskCellHandle(type, data, index, section) {
      //console.log("taskCellHandle")
      if (type == 'view') {
        this.taskID = data.taskId
        this.detailIndex = index
        this.detailSection = section
        this.taskDetailShow = true
      } else if (type == 'complete') {
        const value = this.taskList[section]
        this.changeListCompleteOrder(data, value, index)
        this.updateTaskListCheckNum(value)
      }
    },

    /**
     * 点击空白处关闭详情
     */
    taskShowHandle(e) {
      if (
        this.$refs.particulars &&
        !this.$refs.particulars.$el.contains(e.target)
      ) {
        let hidden = true
        const items = document.getElementsByClassName('board-item')
        for (let index = 0; index < items.length; index++) {
          const element = items[index]
          if (element.contains(e.target)) {
            hidden = false
            break
          }
        }
        this.taskDetailShow = !hidden
      }
    }

  }
}
</script>

<style lang="scss" scoped>
.task-list-board {
  background: #fff;
  height: 100%;
  border: 1px solid #e6e6e6;
  border-radius: $xr-border-radius-base;
  padding: 15px;
  position: relative;
  overflow: auto;
  user-select: none;
}

/deep/ .el-collapse {
  border-top: none;
  border-bottom: none;
  .el-collapse-item__header,
  .el-collapse-item__wrap {
    border-bottom: none;
  }

  .el-collapse-item__content {
    padding-bottom: 0px;
    border-bottom: 1px solid #EBEEF5;
    margin-bottom: 5px;
  }
}

.list-text {
  display: inline-block;
}
.el-progress /deep/ .el-progress-bar {
  padding-right: 0;
}
.el-progress /deep/ .el-progress__text {
  display: none;
}
.list-text-num {
  margin-left: 10px;
  color: #ccc;
}
.img-gd {
  margin-left: 15px;
  color: #ccc;
  cursor: pointer;
}

.omit-popover-box {
  margin-left: -12px;
  margin-right: -12px;
  p {
    font-size: 13px;
    color: #333;
    height: 40px;
    line-height: 40px;
    padding-left: 20px;
    cursor: pointer;
  }
  p:hover {
    background: #f7f8fa;
    color: #2362FB;
  }
}

.task-board-rechristen-popover {
  padding: 0;
  .task-board-rechristen-box {
    .title {
      border-bottom: 1px solid #e6e6e6;
      padding: 15px;
      .el-icon-close {
        margin-right: 0px;
      }
    }
    .content {
      padding: 0 15px;
      .el-input {
        margin: 15px 0;
      }
      .btn-box {
        text-align: right;
        margin-bottom: 15px;
      }
    }
  }
}

// 新建按钮
.new-list-task {
  cursor: pointer;
  color: #999999;
  padding: 10px 0;
  font-size: 13px;
  .el-icon-plus {
    color: #2362FB;
    font-weight: 700;
  }
}

// 新建列表
.board-column-new-list {
  width: 280px;
  background: #fff;
  vertical-align: top;
  display: inline-block;
  border-radius: $xr-border-radius-base;
  .new-list {
    height: 50px;
    line-height: 50px;
    color: #999;
    cursor: pointer;
    .el-icon-plus {
      color: #2362FB;
      font-size: 12px;
      font-weight: 700;
    }
  }
  .input-btn {
    height: 100px;
    padding: 20px;
    .button-box {
      float: right;
      margin-top: 10px;
    }
  }
}
</style>
