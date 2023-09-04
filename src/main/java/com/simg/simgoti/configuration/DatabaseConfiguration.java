package com.simg.simgoti.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;


@Configuration
@PropertySource("classpath:/application.properties")
@RequiredArgsConstructor
public class DatabaseConfiguration {
  private final ApplicationContext app;

  // hikari 커넥션 풀 사용
  @Bean
  @ConfigurationProperties(prefix="spring.datasource.hikari")
  public HikariConfig hikariConfig() {
    return new HikariConfig();
  }

  // 데이터소스: 데이터베이스에 연결하기 위한 객체
  @Bean
  public DataSource dataSource() throws Exception {
    DataSource dataSource = new HikariDataSource(hikariConfig());
    System.out.println(dataSource.toString());
    return dataSource;
  }

  // sqlSession: MyBatis가 sql 쿼리를 실행하기 위한 객체
  // 쿼리 실행, 커밋과 롤백 수행, 데이터베이스 연결 해제 작업을 함
  // sqlSessionFactory: sqlSession을 생성, 데이터베이스 연결과 트랙잭션 관리
  @Bean
  public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
    SqlSessionFactoryBean ssfb = new SqlSessionFactoryBean();
    ssfb.setDataSource(dataSource);
    ssfb.setMapperLocations(app.getResources("classpath:/sql/**/sql-*.xml"));
    ssfb.setConfiguration(mybatisConfig());

    return ssfb.getObject();
  }

  // sqlSessionFactory로 sqlSession을 생성, 관리
  @Bean
  public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory ssf) {
    return new SqlSessionTemplate(ssf);
  }

  // sqlSessionFactory를 구성할 때 스프링부트 외부 설정파일인 application.properties에서 mysql 계정정보 등을 사용할 수 있도록 도와주는 메소드
  @Bean
  @ConfigurationProperties(prefix="mybatis.configuration")
  public org.apache.ibatis.session.Configuration mybatisConfig() {
    return new org.apache.ibatis.session.Configuration();
  }
}












