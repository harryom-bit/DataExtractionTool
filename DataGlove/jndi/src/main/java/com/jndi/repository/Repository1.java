package com.jndi.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jndi.model.meteringDevice;


public interface Repository1 extends JpaRepository<meteringDevice,String>{
	
	@Query(value="select * from METERING_DEVICE where id=?", nativeQuery=true)
	public List<String[]> meteringDeviceAll();
	
	@Query(value="select * from mdvc_cum_reads",nativeQuery=true)
	public List<List<Object>> mdvcCumReads();
	
	@Query(value="select * from table(PKG_RETURN_TABLES.FUNC_RET_ROWS(1))",nativeQuery=true)
	public List<String[]> singleRow();
	
	@Query(value="select * from table(PKG_RETURN_TABLES.FUNC_RET_ROWS(?1,?2,?3,?4,?5))",nativeQuery=true)
	public List<String[]> queryforData(String meterId,String sdpId,Date startDate,Date endDate,String tableArray);
	
	@Query(value="select * from table(PKG_RETURN_TABLES.FUNC_RET_ROWS(1))",nativeQuery=true)
	public List<List<Object>> singleRow2();
	
	
	@Query(value="select TABLE_NAME FROM USER_TABLES",nativeQuery=true)
	public List<String> tables();
	
	@Query(value="select TABLE_NAME from all_tables  where OWNER =?1 AND TABLE_NAME LIKE ?2 ",nativeQuery=true)
	public List<String> getModuleRelatedTables(String owner, String tableAcronym);
}
