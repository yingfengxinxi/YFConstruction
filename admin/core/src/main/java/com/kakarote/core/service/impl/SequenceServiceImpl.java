package com.kakarote.core.service.impl;

import com.kakarote.core.entity.PO.Sequence;
import com.kakarote.core.mapper.SequenceMapper;
import com.kakarote.core.service.ISequenceService;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.BaseUtil;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author pz
 * @since 2021-04-02
 */
@Service
public class SequenceServiceImpl extends BaseServiceImpl<SequenceMapper, Sequence> implements ISequenceService {

    @Override
    public String getDateSeq(String type) {
        String seq = "";
        List<Sequence> list =lambdaQuery().eq(Sequence::getType,type).ge(Sequence::getDate,BaseUtil.getDate())
                .orderByDesc(Sequence::getDate).list();
        Sequence sequenceNew = new Sequence();
        sequenceNew.setType(type);
        sequenceNew.setDate(new Date());
        if (list.size() == 0) {
            sequenceNew.setIndez(1);
            seq = getCode(0);
        } else {
            Sequence sequence = list.get(0);
            sequenceNew.setIndez(sequence.getIndez()+1);
            seq = getCode(sequence.getIndez());
        }
        save(sequenceNew);
        return seq;
    }

    @Override
    public String getDateSeqNew(String type) {
        String seq = "";
        List<Sequence> list =lambdaQuery().eq(Sequence::getType,type).ge(Sequence::getDate,BaseUtil.getDate())
                .orderByDesc(Sequence::getDate).list();
        Sequence sequenceNew = new Sequence();
        sequenceNew.setType(type);
        sequenceNew.setDate(new Date());
        if (list.size() == 0) {
            sequenceNew.setIndez(1);
            seq = getCodeNew(0);
        } else {
            Sequence sequence = list.get(0);
            sequenceNew.setIndez(sequence.getIndez()+1);
            seq = getCodeNew(sequence.getIndez());
        }
        save(sequenceNew);
        return seq;
    }

    @Override
    public String getDateSeq(String type, Integer projectId) {
        String seq = "";
        List<Sequence> list =lambdaQuery().eq(Sequence::getType,type).eq(Sequence::getProjectId, projectId).ge(Sequence::getDate,BaseUtil.getDate())
                .orderByDesc(Sequence::getDate).list();
        Sequence sequenceNew = new Sequence();
        sequenceNew.setType(type);
        sequenceNew.setDate(new Date());
        sequenceNew.setProjectId(projectId);
        if (list.size() == 0) {
            sequenceNew.setIndez(1);
            seq = getCode(0);
        } else {
            Sequence sequence = list.get(0);
            sequenceNew.setIndez(sequence.getIndez()+1);
            seq = getCode(sequence.getIndez());
        }

        save(sequenceNew);

        return seq;
    }

    @Override
    public String getDateSeq(String type, Integer tenantId, Integer projectId) {
        String seq = "";
        List<Sequence> list =lambdaQuery().eq(Sequence::getType,type).eq(Sequence::getTenantId, tenantId).eq(Sequence::getProjectId, projectId).ge(Sequence::getDate,BaseUtil.getDate())
                .orderByDesc(Sequence::getDate).list();
        Sequence sequenceNew = new Sequence();
        sequenceNew.setType(type);
        sequenceNew.setDate(new Date());
        sequenceNew.setTenantId(tenantId);
        sequenceNew.setProjectId(projectId);
        if (list.size() == 0) {
            sequenceNew.setIndez(1);
            seq = getCode(0);
        } else {
            Sequence sequence = list.get(0);
            sequenceNew.setIndez(sequence.getIndez()+1);
            seq = getCode(sequence.getIndez());
        }

        getBaseMapper().insertTenantId(sequenceNew);

        return seq;
    }

    private String getCode(Integer curIndex) {
        String nextCode = BaseUtil.getDate();
        if (curIndex == 0) {
            return nextCode + "001";
        }
        int nextNumber = curIndex + 1;
            if (nextNumber < 10) {
                nextCode += "00" + nextNumber;
            } else if (nextNumber < 100) {
                nextCode += "0" + nextNumber;
            }else{
                nextCode += "" + nextNumber;
            }
        return nextCode;
    }

    private String getCodeNew(Integer curIndex) {
        String nextCode = BaseUtil.getDate();
        if (curIndex == 0) {
            return nextCode + "0001";
        }
        int nextNumber = curIndex + 1;
        if (nextNumber < 10) {
            nextCode += "000" + nextNumber;
        } else if (nextNumber < 100) {
            nextCode += "00" + nextNumber;
        } else if (nextNumber < 1000) {
            nextCode += "0" + nextNumber;
        } else{
            nextCode += "" + nextNumber;
        }
        return nextCode;
    }
}
