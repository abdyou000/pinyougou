package com.pinyougou.seller.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.common.enums.SellerStatusEnum;
import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.mapper.TbSellerDao;
import com.pinyougou.pojo.TbSeller;
import com.pinyougou.pojo.TbSellerExample;
import com.pinyougou.pojo.TbSellerExample.Criteria;
import com.pinyougou.seller.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 服务实现层
 *
 * @author Administrator
 */
@Service
public class SellerServiceImpl implements SellerService {

    @Autowired
    private TbSellerDao sellerMapper;

    /**
     * 查询全部
     */
    @Override
    public List<TbSeller> findAll() {
        return sellerMapper.selectByExample(null);
    }

    /**
     * 按分页查询
     */
    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<TbSeller> page = (Page<TbSeller>) sellerMapper.selectByExample(null);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 增加
     */
    @Override
    public void add(TbSeller seller) {
        //验证sellerId唯一性
        TbSellerExample example = new TbSellerExample();
        Criteria criteria = example.createCriteria();
        criteria.andSellerIdEqualTo(seller.getSellerId());
        List<TbSeller> sellers = sellerMapper.selectByExample(example);
        if (sellers.size() > 0) {
            throw new IllegalArgumentException("用户名已存在");
        }
        seller.setStatus(SellerStatusEnum.NOTAUDIT.getCode());
        seller.setCreateTime(new Date());
        sellerMapper.insert(seller);
    }


    /**
     * 修改
     */
    @Override
    public void update(TbSeller seller) {
        sellerMapper.updateByPrimaryKeySelective(seller);
    }

    @Override
    public void updateStatus(Long id, String status) {
        SellerStatusEnum statusEnum = SellerStatusEnum.valueFrom(status);
        if (Objects.isNull(statusEnum)) {
            throw new IllegalArgumentException("状态不符，非法修改");
        }
        TbSeller seller = sellerMapper.selectByPrimaryKey(id);
        seller.setStatus(status);
        sellerMapper.updateByPrimaryKeySelective(seller);

        //根据审核结果给商家通知如短信邮箱
        //TODO
        switch (statusEnum) {
            case AUDITPASS:
                break;
            case AUDITFAIL:
                break;
            case CLOSED:
                break;
            case NOTAUDIT:
                break;
            default:
                break;
        }
    }

    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    @Override
    public TbSeller findOne(Long id) {
        return sellerMapper.selectByPrimaryKey(id);
    }

    /**
     * 批量删除
     */
    @Override
    public void delete(Long[] ids) {
        TbSellerExample example = new TbSellerExample();
        Criteria criteria = example.createCriteria();
        if (ids != null && ids.length > 0) {
            criteria.andIdIn(Arrays.asList(ids));
            sellerMapper.deleteByExample(example);
        }
    }


    @Override
    public PageResult findPage(TbSeller seller, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        TbSellerExample example = new TbSellerExample();
        Criteria criteria = example.createCriteria();

        if (seller != null) {
            if (seller.getSellerId() != null && seller.getSellerId().length() > 0) {
                criteria.andSellerIdLike("%" + seller.getSellerId() + "%");
            }
            if (seller.getName() != null && seller.getName().length() > 0) {
                criteria.andNameLike("%" + seller.getName() + "%");
            }
            if (seller.getNickName() != null && seller.getNickName().length() > 0) {
                criteria.andNickNameLike("%" + seller.getNickName() + "%");
            }
            if (seller.getPassword() != null && seller.getPassword().length() > 0) {
                criteria.andPasswordLike("%" + seller.getPassword() + "%");
            }
            if (seller.getEmail() != null && seller.getEmail().length() > 0) {
                criteria.andEmailLike("%" + seller.getEmail() + "%");
            }
            if (seller.getMobile() != null && seller.getMobile().length() > 0) {
                criteria.andMobileLike("%" + seller.getMobile() + "%");
            }
            if (seller.getTelephone() != null && seller.getTelephone().length() > 0) {
                criteria.andTelephoneLike("%" + seller.getTelephone() + "%");
            }
            if (seller.getStatus() != null && seller.getStatus().length() > 0) {
                criteria.andStatusLike("%" + seller.getStatus() + "%");
            }
            if (seller.getAddressDetail() != null && seller.getAddressDetail().length() > 0) {
                criteria.andAddressDetailLike("%" + seller.getAddressDetail() + "%");
            }
            if (seller.getLinkmanName() != null && seller.getLinkmanName().length() > 0) {
                criteria.andLinkmanNameLike("%" + seller.getLinkmanName() + "%");
            }
            if (seller.getLinkmanQq() != null && seller.getLinkmanQq().length() > 0) {
                criteria.andLinkmanQqLike("%" + seller.getLinkmanQq() + "%");
            }
            if (seller.getLinkmanMobile() != null && seller.getLinkmanMobile().length() > 0) {
                criteria.andLinkmanMobileLike("%" + seller.getLinkmanMobile() + "%");
            }
            if (seller.getLinkmanEmail() != null && seller.getLinkmanEmail().length() > 0) {
                criteria.andLinkmanEmailLike("%" + seller.getLinkmanEmail() + "%");
            }
            if (seller.getLicenseNumber() != null && seller.getLicenseNumber().length() > 0) {
                criteria.andLicenseNumberLike("%" + seller.getLicenseNumber() + "%");
            }
            if (seller.getTaxNumber() != null && seller.getTaxNumber().length() > 0) {
                criteria.andTaxNumberLike("%" + seller.getTaxNumber() + "%");
            }
            if (seller.getOrgNumber() != null && seller.getOrgNumber().length() > 0) {
                criteria.andOrgNumberLike("%" + seller.getOrgNumber() + "%");
            }
            if (seller.getLogoPic() != null && seller.getLogoPic().length() > 0) {
                criteria.andLogoPicLike("%" + seller.getLogoPic() + "%");
            }
            if (seller.getBrief() != null && seller.getBrief().length() > 0) {
                criteria.andBriefLike("%" + seller.getBrief() + "%");
            }
            if (seller.getLegalPerson() != null && seller.getLegalPerson().length() > 0) {
                criteria.andLegalPersonLike("%" + seller.getLegalPerson() + "%");
            }
            if (seller.getLegalPersonCardId() != null && seller.getLegalPersonCardId().length() > 0) {
                criteria.andLegalPersonCardIdLike("%" + seller.getLegalPersonCardId() + "%");
            }
            if (seller.getBankUser() != null && seller.getBankUser().length() > 0) {
                criteria.andBankUserLike("%" + seller.getBankUser() + "%");
            }
            if (seller.getBankName() != null && seller.getBankName().length() > 0) {
                criteria.andBankNameLike("%" + seller.getBankName() + "%");
            }

        }

        Page<TbSeller> page = (Page<TbSeller>) sellerMapper.selectByExample(example);
        return new PageResult(page.getTotal(), page.getResult());
    }

}
