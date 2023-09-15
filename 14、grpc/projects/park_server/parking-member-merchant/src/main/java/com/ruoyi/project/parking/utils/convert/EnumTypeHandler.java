package com.ruoyi.project.parking.utils.convert;

import com.ruoyi.project.parking.enums.BaseEnum;
import com.ruoyi.project.parking.utils.BaseEnumUtil;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 枚举映射处理器
 *
 * @param <E>
 */
public class EnumTypeHandler<E extends Enum<?> & BaseEnum> extends BaseTypeHandler<BaseEnum> {
    private Class<E> clazz;

    public EnumTypeHandler(Class<E> enumType) {
        if (enumType == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        this.clazz = enumType;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, BaseEnum parameter, JdbcType jdbcType)
            throws SQLException {
        ps.setString(i, parameter.getValue());
    }

    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return BaseEnumUtil.valueOf(clazz, rs.getString(columnName));
    }

    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return BaseEnumUtil.valueOf(clazz, rs.getString(columnIndex));
    }

    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return BaseEnumUtil.valueOf(clazz, cs.getString(columnIndex));
    }
}