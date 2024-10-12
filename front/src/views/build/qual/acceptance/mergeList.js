//数据合并
export default {
    methods: {
        getListDataForRowAndColumn(data){
            let self = this;
            self.rowAndColumn = [];
            self.rowRoomColumn = [];
            self.row3Column = [];
            self.row4Column = [];
            self.row5Column = [];
            for (var i = 0; i < data.length; i++) {
                if (i === 0) {
                    // 如果是第一条记录（即索引是0的时候），向数组中加入１
                    self.rowAndColumn.push(1);
                    self.pos = 0;
                    self.rowRoomColumn.push(1);
                    self.posT = 0;
                    self.row3Column.push(1);
                    self.pos3 = 0;
                    self.row4Column.push(1);
                    self.pos4 = 0;
                    self.row5Column.push(1);
                    self.pos5 = 0;
                } else {
                    if (data[i].unitP === data[i - 1].unitP) {
                        // 如果storeName相等就累加，并且push 0
                        self.rowAndColumn[self.pos] += 1
                        self.rowAndColumn.push(0)
                        if (data[i].subUnitP === data[i - 1].subUnitP) {
                            // 如果roomName相等就累加，并且push 0
                            self.rowRoomColumn[self.posT] += 1
                            self.rowRoomColumn.push(0)
                            if (data[i].parcelP === data[i - 1].parcelP) {
                                self.row3Column[self.pos3] += 1
                                self.row3Column.push(0)
                                if (data[i].subParcelP === data[i - 1].subParcelP) {
                                    self.row4Column[self.pos4] += 1
                                    self.row4Column.push(0)
                                    if (data[i].itemP === data[i - 1].itemP) {
                                        self.row5Column[self.pos5] += 1
                                        self.row5Column.push(0)
                                    }else{
                                        self.row5Column.push(1)
                                        self.pos5 = i
                                    }
                                }else{
                                    self.row4Column.push(1)
                                    self.pos4 = i
                                    self.row5Column.push(1)
                                    self.pos5 = i
                                }
                            }else{
                                self.row3Column.push(1)
                                self.pos3 = i
                                self.row4Column.push(1)
                                self.pos4 = i
                                self.row5Column.push(1)
                                self.pos5 = i
                            }
                        }else{
                            self.rowRoomColumn.push(1)
                            self.posT = i
                            self.row3Column.push(1)
                            self.pos3 = i
                            self.row4Column.push(1)
                            self.pos4 = i
                            self.row5Column.push(1)
                            self.pos5 = i
                        }
                    } else {
                        // 不相等push 1
                        self.rowAndColumn.push(1)
                        self.pos = i;
                        self.rowRoomColumn.push(1)
                        self.posT = i
                        self.row3Column.push(1)
                        self.pos3 = i
                        self.row4Column.push(1)
                        self.pos4 = i
                        self.row5Column.push(1)
                        self.pos5 = i
                    }
                }
            }
        },
        objectSpanMethod({ row, column, rowIndex, columnIndex }) {
            let self = this
            if (columnIndex === 2) {
                if (self.rowAndColumn[rowIndex]) {
                    let rowNum = self.rowAndColumn[rowIndex];
                    return {
                        rowspan: rowNum,
                        colspan: rowNum > 0 ? 1 : 0
                    }
                }
                return {
                    rowspan: 0,
                    colspan: 0
                }
            }
            if (columnIndex === 3) {
                if (self.rowRoomColumn[rowIndex]) {
                    let roomNum = self.rowRoomColumn[rowIndex];
                    return {
                        rowspan: roomNum,
                        colspan: roomNum > 0 ? 1 : 0
                    }
                }
                return {
                    rowspan: 0,
                    colspan: 0
                }
            }
            if (columnIndex === 4) {
                if (self.row3Column[rowIndex]) {
                    let roomNum = self.row3Column[rowIndex];
                    return {
                        rowspan: roomNum,
                        colspan: roomNum > 0 ? 1 : 0
                    }
                }
                return {
                    rowspan: 0,
                    colspan: 0
                }
            }
            if (columnIndex === 5) {
                if (self.row4Column[rowIndex]) {
                    let roomNum = self.row4Column[rowIndex];
                    return {
                        rowspan: roomNum,
                        colspan: roomNum > 0 ? 1 : 0
                    }
                }
                return {
                    rowspan: 0,
                    colspan: 0
                }
            }
            if (columnIndex === 6) {
                if (self.row5Column[rowIndex]) {
                    let roomNum = self.row5Column[rowIndex];
                    return {
                        rowspan: roomNum,
                        colspan: roomNum > 0 ? 1 : 0
                    }
                }
                return {
                    rowspan: 0,
                    colspan: 0
                }
            }
        }
    }
}