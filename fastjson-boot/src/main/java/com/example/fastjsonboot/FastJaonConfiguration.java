package com.example.fastjsonboot;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

/**
 * SerializerFeature.WriteMapNullValue 是否输出值为null的字段,默认为false
 * 也就是说有null时会输出而不是忽略（默认策略是忽略，所以看不到为null的字段）
 *
 * WriteNullStringAsEmpty 字符类型字段如果为null,输出为”“,而非null
 * 注意是字段！是字段！是字段！，而不是json.put("key",null)，所以用它时，字段为null的可以转换为空字符串。
 */
@Configuration
public class FastJaonConfiguration extends WebMvcConfigurationSupport{

    @Override
    protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        super.configureMessageConverters(converters);

        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();

        FastJsonConfig fastJsonConfig = new FastJsonConfig();

        fastJsonConfig.setSerializerFeatures(
                SerializerFeature.DisableCircularReferenceDetect,
//                SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.WriteNullStringAsEmpty
        );

        fastConverter.setFastJsonConfig(fastJsonConfig);

        converters.add(fastConverter);
    }
}
