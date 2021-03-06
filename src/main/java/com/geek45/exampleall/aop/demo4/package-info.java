/**
 * 通过aop实现权限管理
 *
 * 环绕通知获取方法上注解的权限信息
 *
 * 接口到达之前，拦截掉请求，获取请求携带的token，判断角色信息，验证是否有权限。
 *
 * 如果没有权限，返回相关的操作，如果有权限，就继续执行接口请求。将接口请求返回值返回。
 *
 * @author rubik
 *
 *
 */
package com.geek45.exampleall.aop.demo4;