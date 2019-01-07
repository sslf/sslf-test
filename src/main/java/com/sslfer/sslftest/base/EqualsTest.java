package com.sslfer.sslftest.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author sslf
 * @date 2018/11/28
 */
public class EqualsTest {

    @Getter
    @Setter
    @AllArgsConstructor
    static class Person {
        private String name;
        private Integer age;

        @Override
        public boolean equals(Object obj) {
            return super.equals(obj);
        }
    }

    public static void main(String[] args) {

        Person a = new Person("a", 1);
        Person b = new Person("a", 1);

        System.out.println(a == b);
        System.out.println(a.equals(b));

    }

}
