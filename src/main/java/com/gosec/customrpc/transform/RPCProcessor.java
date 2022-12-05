package com.gosec.customrpc.transform;

import com.gosec.customrpc.annotation.RPCService;
import com.gosec.customrpc.server.annotation.RpcServer;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Set;
public class RPCProcessor extends AbstractProcessor {
    private Messager messager;
    @Override
    public synchronized boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> set = roundEnv.getElementsAnnotatedWith(RpcServer.class);
        ClassUtils.FieldAnnotationScan("com.gosec.customrpc.client", RPCService.class);
        return true;
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.messager  = processingEnv.getMessager();
    }
}
