package com.example.demo.aspect;
/*
 * Date: 12/30/20
 * Time: 1:26 PM
 * */

import com.example.demo.entity.Question;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class QuestionViewAspect {

    // todo pay attention
    @AfterReturning(pointcut = "execution(* com.example.demo.controller.UserController.getQuestionsPage())")
    public void afterReturningFindQuestionByIdAdvice() {
        System.out.println(">>>>>>>>>>");
        System.out.println("Delete later");
        System.out.println(">>>>>>>>>>");

    }
}
