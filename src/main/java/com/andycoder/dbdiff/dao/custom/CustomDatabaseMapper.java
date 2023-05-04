package com.andycoder.dbdiff.dao.custom;

import com.andycoder.dbdiff.dao.DatabaseMapper;
import com.andycoder.dbdiff.dto.Column;
import com.andycoder.dbdiff.dto.Index;
import com.andycoder.dbdiff.dto.Table;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CustomDatabaseMapper extends DatabaseMapper {

}
