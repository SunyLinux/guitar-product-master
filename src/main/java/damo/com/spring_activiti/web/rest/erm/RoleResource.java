package damo.com.spring_activiti.web.rest.erm;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import damo.com.spring_activiti.domain.erm.Role;
import damo.com.spring_activiti.domain.erm.RoleUser;
import damo.com.spring_activiti.service.erm.RoleService;
import damo.com.spring_activiti.service.erm.RoleUserService;
import damo.com.spring_activiti.service.erm.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import damo.com.spring_activiti.domain.erm.Role;
import damo.com.spring_activiti.domain.erm.RoleUser;
import damo.com.spring_activiti.service.erm.RoleService;
import damo.com.spring_activiti.service.erm.RoleUserService;
import damo.com.spring_activiti.service.erm.UserService;

/**
 * Created by suny on 2017/12/27.
 */
@RestController
@RequestMapping("/api")
public class RoleResource {
    private final Logger log =LoggerFactory.getLogger(RoleResource.class);
    @Autowired
   private RoleService roleService;
    @Autowired
    private RoleUserService roleUserService;
    @Autowired
    private UserService userService;

    /** Query All Roles*/
    @RequestMapping(value = "/RolefindAll",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Role>> tree(Long id) {
        if(id==null){
            return new ResponseEntity(roleService.findAll(), HttpStatus.OK) ;
        }else{
            return new ResponseEntity(roleService.findOne(id),HttpStatus.OK) ;
        }
    }

    /**Roles Save */
    @RequestMapping(value = "/saveRole",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Role> saveRole(Role role)throws URISyntaxException {
        return new ResponseEntity(roleService.save(role),HttpStatus.OK) ;
    }

    /**Roles Delete */
   @RequestMapping(value = "/deleteRole",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Map<String,String>> deleteRole(Long id) {
         roleService.deleteRole(id);
        Map<String,String> map=new HashMap<String,String>();
        map.put("success","success");
        return new ResponseEntity(map,HttpStatus.OK) ;
    }

    /** UserList */
    @RequestMapping(value = "/userInfo/findByRole",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<RoleUser>> findByRole(Long roleId) throws URISyntaxException {
       if (roleId!=null){
            //Query
            return new ResponseEntity(roleUserService.findByRoleId(roleId), HttpStatus.OK);
        }
        else{
           return new ResponseEntity(null, HttpStatus.OK);
       }
    }

    /** UserList  Save */
    @RequestMapping(value = "/saveRoleUserList",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<RoleUser>>  saveRoleUserList(Long [] userId,Long roleId)throws URISyntaxException {
        List<RoleUser> RoleUserList=new ArrayList<>();
       for(Long u:userId ){
           RoleUser  roleUser=new RoleUser();
           if(roleUserService.findUserByRoleId(u,roleId).size()==0) {
               roleUser.setUser(userService.findById(u));
               roleUser.setRole(roleService.findOne(roleId));
               RoleUserList.add(roleUserService.save(roleUser));
           }
       }
        return new ResponseEntity(RoleUserList,HttpStatus.OK) ;
    }

    /** UserList  Delete */
    @RequestMapping(value = "/deleteRoleUser",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Map<String,String>> deleteRoleUser(Long[] roleUserList) {
        for(Long u:roleUserList){
            roleUserService.deleteById(u);
        }
        Map<String,String> map=new HashMap<String,String>();
        map.put("success","success");
        return new ResponseEntity(map,HttpStatus.OK) ;
    }

    /**Roles Search*/
    @RequestMapping(value = "/searchRole",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Role>> searchRole(String name) {
        return new ResponseEntity(roleService.findByNameLike(name),HttpStatus.OK) ;
    }

    /**Roles Search*/
    @RequestMapping(value = "/searchRoleUser",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<RoleUser>> searchRoleUser(String name,Long roleId) {
        return new ResponseEntity(roleUserService.findByNameLike(name,roleId),HttpStatus.OK) ;
    }
}
