package com.gosec.customrpc.server.methodMatch;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

@Component
public class SpringMethodMatcher extends BaseMethodMatcher implements BeanFactoryAware {
    private DefaultListableBeanFactory beanFactory;
    @Override
    public HostClassMethodInfo findHostMethodInfo(Class<?> interfaceClass) {
        HostClassMethodInfo info = new HostClassMethodInfo();
        Object bean = beanFactory.getBean(interfaceClass);
        info.setHostTarget(bean);
        info.setHostKlass(bean.getClass());
        info.setHostUserklass(ClassUtils.getUserClass(bean.getClass()));
        return info;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
    }

}
