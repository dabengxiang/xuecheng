<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Hello World!</title>
</head>
<body>
Hello ${name}!

<br/>

<table>
    <tr>
        <td>序号</td>
        <td>姓名</td>
        <td>年龄</td>
        <td>钱包</td>
    </tr>
    <#list stus as stu>
        <tr>
            <td>${stu_index + 1}</td>
            <td <#if stu.name == '小明'> style="background-color: red" </#if>>${stu.name }</td>
            <td>${stu.age }</td>
            <td>${stu.mondy }</td>
        </tr>
    </#list>
    
</table>
    
    
    <!-- map 的遍历 -->
    <br/><br/>
    输出stu1的学生信息: ${stuMap.stu1.name} <br/>
        ${stuMap.stu1.age} <br/>
    <br/>
    换种写法输出stu1的学生信息: ${stuMap['stu1'].name} <br/>
                            ${stuMap['stu1'].age} <br/>

    map遍历：<br/>

<table>
    <tr>
        <td>序号</td>
        <td>姓名</td>
        <td>年龄</td>
        <td>钱包</td>
    </tr>
    <#list stuMap?keys as k >
        <tr>
            <td>${k_index + 1}</td>
            <td <#if stuMap[k].name == '小明'> style="background-color: red" </#if>>${stuMap[k].name }</td>
            <td>${stuMap[k].age }</td>
            <td>${stuMap[k].mondy }</td>
        </tr>
    </#list>

</table>  
    
    

</body>
</html>