package com.miaosha;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

/**
 * Hello world!
 *
 */

@SpringBootApplication(scanBasePackages = {"com.miaosha"})
@RestController
@MapperScan("com.miaosha.dao")
public class App {

    public static void main( String[] args )
    {

   // SpringApplication.run(App.class,args);



    }

    // System.out.println( "Hello World!" );
//    @Autowired
//    private UserDOMapper userDOMapper;
//    @RequestMapping("/")
//    public String home(){
//       UserDO userDO = userDOMapper.selectByPrimaryKey(1);
//       if(userDO == null){
//           return "用户对象不存在";
//       }
//       else {
//          return userDO.getName();
//       }
//
//    }


}
