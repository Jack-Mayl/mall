package com.longzai.mall.service.impl;

import com.longzai.mall.exception.LongZaiMallException;
import com.longzai.mall.exception.LongZaiMallExceptionEnum;
import com.longzai.mall.model.dao.CategoryMapper;
import com.longzai.mall.model.pojo.Category;
import com.longzai.mall.model.request.AddCategoryReq;
import com.longzai.mall.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @className: CategoryService
 * @description: 目录分类Service实现类
 * @author: Jack.Myl
 * @date: 2022/09/16 2:27 PM
 **/
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryMapper categoryMapper;
    @Override
    public void add(AddCategoryReq addCategoryReq){
        Category category = new Category();
        // Bean的拷贝   拷贝源  拷贝地
        BeanUtils.copyProperties(addCategoryReq,category);
        Category categoryOld = categoryMapper.selectByName(addCategoryReq.getName());
        if (categoryOld !=null){
            throw  new LongZaiMallException(LongZaiMallExceptionEnum.NAME_EXISTED);
        }
        int i = categoryMapper.insertSelective(category);
        if(i == 0){
            throw  new LongZaiMallException(LongZaiMallExceptionEnum.CREATE_FAILED);
        }
    }
}
