一个关于Spring Security与JWT集成的demo, 详细参考[我的笔记](https://github.com/sidian123/notes/blob/master/Spring/Security.md)

这些给出部分前端测试代码:
```vue
<template>
    <div>
        <el-button @click="clickBtn">用户登录</el-button>
        <el-button @click="adminLogin">管理员登录</el-button>
        <el-button @click="clickBtn2">用户信息</el-button>
        <el-button @click="clickBtn3">管理员信息</el-button>
    </div>

</template>
<script>
import  Axios from "axios";

export default {
    name:"Index",
    data(){
        return {
            token:null
        }
    },
    methods:{
        clickBtn(){
            Axios.get("/api/login",{
                params:{
                    username:"luo",
                    password:"123456"
                }
            })
                .then(res=>{
                    console.log("acquire a token:\n"+res.data);
                    //获取token
                    this.token=res.data;
                });

        },
        clickBtn2(){
            Axios.get("/api/user",{
                headers:{
                    "Authorization":"Bearer "+this.token
                }
            })
                .then(res=>console.log(res.data))
                .catch(res=>console.log(res))
        },
        clickBtn3(){
            Axios.get("/api/admin",{
                headers:{
                    "Authorization":"Bearer "+this.token
                }
            })
                .then(res=>console.log(res.data))
                .catch(res=>console.log(res))
        },
        adminLogin(){
            Axios.get("/api/login",{
                params:{
                    username:"lou",
                    password:"123456"
                }
            })
                .then(res=>{
                    console.log("acquire a token:\n"+res.data);
                    //获取token
                    this.token=res.data;
                });
        }

    }
}
</script>
<style>

</style>
```