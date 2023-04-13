package cn.bhshare.meitu.dao;

import cn.bhshare.meitu.model.Model;
import cn.bhshare.meitu.model.ModelExample;
import cn.bhshare.meitu.model.Photo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface NewMapper {

    @Select("select * from model limit #{start},${size};")
    List<Model> getModelList(@Param("start") int start, @Param("size") int size);


    @Select("select * from photo where ${where} limit #{start},${size};")
    List<Photo> getPhotoList(@Param("start") int start, @Param("size") int size, @Param("where") String where);

    @Select("select count(*) from photo where ${where}")
    long getCount(@Param("where") String where);

}