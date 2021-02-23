package com.example.demo.service

import com.example.demo.dao.UserMapper
import com.example.demo.entity.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class Service {
    @Autowired
    private lateinit var mapper:UserMapper

    @CachePut(value = ["user"],key = "#user.id")
    fun save(user:User): User {
        val oldUser = mapper.selectById(user.id)
        if(oldUser == null){
            mapper.insert(user)
            return user
        }
        if(mapper.updateById(user) == 1)
            return user
        return oldUser
    }

    @CacheEvict(value = ["user"],key = "#id")
    fun delete(id:Int) = (mapper.deleteById(id) == 1)

    @Cacheable(value = ["user"],key = "#id")
    fun select(id:Int?): User? = mapper.selectById(id)

    @Cacheable(value = ["allUser"],key = "#root.target+#root.methodName")
    fun selectAll(): List<User>? = mapper.selectList(null)
}