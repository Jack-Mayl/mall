package com.longzai.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.longzai.mall.exception.LongZaiMallException;
import com.longzai.mall.exception.LongZaiMallExceptionEnum;
import com.longzai.mall.model.dao.CategoryMapper;
import com.longzai.mall.model.pojo.Category;
import com.longzai.mall.model.request.AddCategoryReq;
import com.longzai.mall.model.vo.CategoryVo;
import com.longzai.mall.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public void delete(Integer id){
        Category categoryOld = categoryMapper.selectByPrimaryKey(id);
        // 查不到记录 无法删除 删除失败
        if(categoryOld == null){
            throw new LongZaiMallException(LongZaiMallExceptionEnum.DELETE_FAILED);
        }
        int i = categoryMapper.deleteByPrimaryKey(id);
        if (i == 0){
            throw new LongZaiMallException(LongZaiMallExceptionEnum.DELETE_FAILED);
        }
    }
    @Override
    public PageInfo listForAdmin(Integer pageNum, Integer pageSize){
        PageHelper.startPage(pageNum,pageSize,"type,order_num");
        List<Category> categories = categoryMapper.selectList();
        PageInfo pageInfo = new PageInfo(categories);
        return pageInfo;
    }

    @Override
    @Cacheable(value = "listCategoryForCustomer")
    public List<CategoryVo> listCategoryForCustomer(){
        ArrayList<CategoryVo> categoryVos = new ArrayList<>();
        recursivelyFindCategories(categoryVos,0);
        return categoryVos;
    }
    private void recursivelyFindCategories(List<CategoryVo> categoryVos,Integer parenId){
        // 递归获取所有子类别 并组合成为一个"目录树"
        List<Category> categories = categoryMapper.selectCategoriesByParentId(parenId);
        if(!CollectionUtils.isEmpty(categories)){
            for (int i = 0; i <categories.size() ; i++) {
                Category category = categories.get(i);
                CategoryVo categoryVo = new CategoryVo();
                BeanUtils.copyProperties(category,categoryVo);
                categoryVos.add(categoryVo);
                recursivelyFindCategories(categoryVo.getChildCategory(),categoryVo.getId());
            }

        }


    }
}
