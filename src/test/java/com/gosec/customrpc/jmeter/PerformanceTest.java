package com.gosec.customrpc.jmeter;

import com.gosec.customrpc.client.Client;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

public class PerformanceTest extends AbstractJavaSamplerClient {

    private SampleResult result;
    private String name;
    Client client = new Client();
    /**
     * 初始化方法，用于初始化性能测试时的每个线程，每个线程测试前执行一次。
     */
    @Override
    public void setupTest(JavaSamplerContext context) {
        result = new SampleResult();

        name = context.getParameter("name");

        // 可以初始化 RPC Client
    }

    /**
     * 测试结束时调用，可释放资源等。
     */
    @Override
    public void teardownTest(JavaSamplerContext context) {
        System.out.println("NormalJavaRequestSample.teardownTest");
    }

    /**
     * 主要用于设置传入的参数和默认值，可在 Jmeter 界面显示。
     */
    @Override
    public Arguments getDefaultParameters() {
        Arguments arguments = new Arguments();

        arguments.addArgument("name", "performance");

        return arguments;
    }

    /**
     * 性能测试运行体。
     */
    @Override
    public SampleResult runTest(JavaSamplerContext context) {
        result.sampleStart(); // Jmeter 开始计时

        boolean success = hello(context.getParameter("name"));

        result.setSuccessful(success); // 是否成功
        result.sampleEnd(); // Jmeter 结束计时

        return result;
    }

    private boolean hello(String name) {
        client.hello(name);
        return true;
    }

    /**
     * Jmeter 不会调用 main 方法，这里用于生成 Jar。
     * @param args
     */
    public static void main(String[] args) {
        Arguments arguments = new Arguments();
        arguments.addArgument("name", "performance");

        JavaSamplerContext context = new JavaSamplerContext(arguments);
        PerformanceTest sample = new PerformanceTest();
        sample.setupTest(context);
        sample.runTest(context);
        sample.teardownTest(context);
    }
}

