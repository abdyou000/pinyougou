package com.pinyougou.seller.auth;

import com.alibaba.dubbo.config.annotation.Reference;
import com.google.common.collect.Lists;
import com.pinyougou.common.enums.SellerStatusEnum;
import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.pojo.TbSeller;
import com.pinyougou.seller.service.SellerService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Objects;

public class UserDetailsServiceImpl implements UserDetailsService {

    private SellerService sellerService;

    public void setSellerService(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        TbSeller criteria = new TbSeller();
        criteria.setSellerId(username);
        PageResult page = sellerService.findPage(criteria, 1, 1);
        List<TbSeller> sellers = page.getRows();
        //用户不存在
        if (Objects.isNull(sellers) || sellers.isEmpty()) {
            return null;
        }
        TbSeller seller = sellers.get(0);
        String auditPassStatus = String.valueOf(SellerStatusEnum.AUDITPASS.ordinal());
        //用户审核未通过
        if (!Objects.equals(auditPassStatus,seller.getStatus())) {
            return null;
        }
        List<GrantedAuthority> roles = Lists.newArrayList(
                new SimpleGrantedAuthority("ROLE_SELLER")
        );
        return new User(username, seller.getPassword(), roles);
    }
}
