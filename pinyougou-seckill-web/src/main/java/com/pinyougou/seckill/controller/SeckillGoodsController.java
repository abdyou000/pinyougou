package com.pinyougou.seckill.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.common.pojo.ResponseResult;
import com.pinyougou.pojo.TbSeckillGoods;
import com.pinyougou.seckill.service.SeckillGoodsService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * controller
 *
 * @author Administrator
 */
@RestController
@RequestMapping("/seckillGoods")
public class SeckillGoodsController {

    @Reference(timeout = 10000)
    private SeckillGoodsService seckillGoodsService;

    /**
     * 返回全部列表
     *
     * @return
     */
    @RequestMapping("/findAll")
    public List<TbSeckillGoods> findAll() {
        return seckillGoodsService.findAll();
    }


    /**
     * 返回全部列表
     *
     * @return
     */
    @RequestMapping("/findPage")
    public ResponseResult findPage(int page, int rows) {
        PageResult pageResult = seckillGoodsService.findPage(page, rows);
        return ResponseResult.ok(pageResult);
    }

    /**
     * 增加
     *
     * @param seckillGoods
     * @return
     */
    @RequestMapping("/add")
    public ResponseResult add(@RequestBody TbSeckillGoods seckillGoods) {
        try {
            seckillGoodsService.add(seckillGoods);
            return ResponseResult.ok("增加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error("增加失败");
        }
    }

    /**
     * 修改
     *
     * @param seckillGoods
     * @return
     */
    @RequestMapping("/update")
    public ResponseResult update(@RequestBody TbSeckillGoods seckillGoods) {
        try {
            seckillGoodsService.update(seckillGoods);
            return ResponseResult.ok("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error("修改失败");
        }
    }

    /**
     * 获取实体
     *
     * @param id
     * @return
     */
    @RequestMapping("/findOne")
    public TbSeckillGoods findOne(Long id) {
        return seckillGoodsService.findOne(id);
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @RequestMapping("/delete")
    public ResponseResult delete(Long[] ids) {
        try {
            seckillGoodsService.delete(ids);
            return ResponseResult.ok("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error("删除失败");
        }
    }

    /**
     * 查询+分页
     *
     * @param seckillGoods
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("/search")
    public ResponseResult search(@RequestBody TbSeckillGoods seckillGoods, int page, int rows) {
        PageResult pageResult = seckillGoodsService.findPage(seckillGoods, page, rows);
        return ResponseResult.ok(pageResult);
    }


    @RequestMapping("/findList")
    public ResponseResult findList() {
        List<TbSeckillGoods> goodsList = seckillGoodsService.findList();
        return ResponseResult.ok(goodsList);
    }

    @RequestMapping("/findOneFromRedis")
    public ResponseResult findOneFromRedis(Long id) {
        TbSeckillGoods seckillGoods = seckillGoodsService.findOneFromRedis(id);
        return ResponseResult.ok(seckillGoods);
    }
}
