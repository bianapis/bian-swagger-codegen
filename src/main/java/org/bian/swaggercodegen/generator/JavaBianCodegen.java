package org.bian.swaggercodegen.generator;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;

import io.swagger.codegen.CliOption;
import io.swagger.codegen.CodegenConstants;
import io.swagger.codegen.CodegenModel;
import io.swagger.codegen.CodegenOperation;
import io.swagger.codegen.CodegenParameter;
import io.swagger.codegen.CodegenProperty;
import io.swagger.codegen.CodegenResponse;
import io.swagger.codegen.CodegenSecurity;
import io.swagger.codegen.CodegenType;
import io.swagger.codegen.SupportingFile;
import io.swagger.codegen.languages.AbstractJavaCodegen;
import io.swagger.codegen.languages.features.BeanValidationFeatures;
import io.swagger.codegen.languages.features.OptionalFeatures;
import io.swagger.models.Model;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Swagger;


// TODO: Auto-generated Javadoc
/**
 * The Class JavaBianCodegen.
 */
public class JavaBianCodegen extends AbstractJavaCodegen
        implements BeanValidationFeatures, OptionalFeatures {
	
    /** The logger. */
    static Logger LOGGER = LoggerFactory.getLogger(JavaBianCodegen.class);
    
    /** The Constant DEFAULT_LIBRARY. */
    public static final String DEFAULT_LIBRARY = "spring-boot";
    
    /** The Constant TITLE. */
    public static final String TITLE = "title";
    
    /** The Constant CONFIG_PACKAGE. */
    public static final String CONFIG_PACKAGE = "configPackage";
    
    /** The Constant BASE_PACKAGE. */
    public static final String BASE_PACKAGE = "basePackage";
    
    /** The Constant INTERFACE_ONLY. */
    public static final String INTERFACE_ONLY = "interfaceOnly";
    
    /** The Constant DELEGATE_PATTERN. */
    public static final String DELEGATE_PATTERN = "delegatePattern";
    
    /** The Constant SINGLE_CONTENT_TYPES. */
    public static final String SINGLE_CONTENT_TYPES = "singleContentTypes";
    
    /** The Constant JAVA_8. */
    public static final String JAVA_8 = "java8";
    
    /** The Constant ASYNC. */
    public static final String ASYNC = "async";
    
    /** The Constant RESPONSE_WRAPPER. */
    public static final String RESPONSE_WRAPPER = "responseWrapper";
    
    /** The Constant USE_TAGS. */
    public static final String USE_TAGS = "useTags";
    
    /** The Constant SPRING_MVC_LIBRARY. */
    public static final String SPRING_MVC_LIBRARY = "spring-mvc";
    
    /** The Constant SPRING_CLOUD_LIBRARY. */
    public static final String SPRING_CLOUD_LIBRARY = "spring-cloud";
    
    /** The Constant IMPLICIT_HEADERS. */
    public static final String IMPLICIT_HEADERS = "implicitHeaders";
    
    /** The Constant SWAGGER_DOCKET_CONFIG. */
    public static final String SWAGGER_DOCKET_CONFIG = "swaggerDocketConfig";

    /** The title. */
    protected String title = "swagger-petstore";
    
    /** The config package. */
    protected String configPackage = "io.swagger.configuration";
    
    /** The base package. */
    protected String basePackage = "org.bian";
    
    /** The dto package. */
    protected String dtoPackage = "org.bian.dto";
    
    /** The controller package. */
    protected String controllerPackage = "org.bian.controller";
    
    /** The service package. */
    protected String servicePackage = "org.bian.service";
    
    /** The util package. */
    protected String utilPackage = "org.bian.impl.util";
    
    /** The interface only. */
    protected boolean interfaceOnly = false;
    
    /** The delegate pattern. */
    protected boolean delegatePattern = false;
    
    /** The delegate method. */
    protected boolean delegateMethod = false;
    
    /** The single content types. */
    protected boolean singleContentTypes = false;
    
    /** The java 8. */
    protected boolean java8 = false;
    
    /** The async. */
    protected boolean async = false;
    
    /** The response wrapper. */
    protected String responseWrapper = "";
    
    /** The use tags. */
    protected boolean useTags = false;
    
    /** The use bean validation. */
    protected boolean useBeanValidation = true;
    
    /** The implicit headers. */
    protected boolean implicitHeaders = false;
    
    /** The swagger docket config. */
    protected boolean swaggerDocketConfig = false;
    
    /** The use optional. */
    protected boolean useOptional = false;

    /**
     * Instantiates a new java bian codegen.
     */
    public JavaBianCodegen() {
        super();
        apiTemplateFiles.remove("api.mustache");
        outputFolder = "generated-code/javaSpring";
        apiTestTemplateFiles.clear(); // TODO: add test template
        embeddedTemplateDir = templateDir = "JavaBian";
        apiPackage = "org.bian.service";
        modelPackage = "org.bian.dto";
        invokerPackage = "org.bian";
        artifactId = "swagger-spring";

        additionalProperties.put(CONFIG_PACKAGE, configPackage);
        additionalProperties.put(BASE_PACKAGE, basePackage);
        additionalProperties.put("controllerPackage", controllerPackage);
        additionalProperties.put("utilPackage", utilPackage);

        // spring uses the jackson lib
        additionalProperties.put("jackson", "true");

        cliOptions.add(new CliOption(TITLE, "server title name or client service name"));
        cliOptions.add(new CliOption(CONFIG_PACKAGE, "configuration package for generated code"));
        cliOptions.add(new CliOption(BASE_PACKAGE, "base package (invokerPackage) for generated code"));
        cliOptions.add(CliOption.newBoolean(INTERFACE_ONLY, "Whether to generate only API interface stubs without the server files."));
        cliOptions.add(CliOption.newBoolean(DELEGATE_PATTERN, "Whether to generate the server files using the delegate pattern"));
        cliOptions.add(CliOption.newBoolean(SINGLE_CONTENT_TYPES, "Whether to select only one produces/consumes content-type by operation."));
        cliOptions.add(CliOption.newBoolean(JAVA_8, "use java8 default interface"));
        cliOptions.add(CliOption.newBoolean(ASYNC, "use async Callable controllers"));
        cliOptions.add(new CliOption(RESPONSE_WRAPPER, "wrap the responses in given type (Future,Callable,CompletableFuture,ListenableFuture,DeferredResult,HystrixCommand,RxObservable,RxSingle or fully qualified type)"));
        cliOptions.add(CliOption.newBoolean(USE_TAGS, "use tags for creating interface and controller classnames"));
        cliOptions.add(CliOption.newBoolean(USE_BEANVALIDATION, "Use BeanValidation API annotations"));
        cliOptions.add(CliOption.newBoolean(IMPLICIT_HEADERS, "Use of @ApiImplicitParams for headers."));
        cliOptions.add(CliOption.newBoolean(SWAGGER_DOCKET_CONFIG, "Generate Spring Swagger Docket configuration class."));
        cliOptions.add(CliOption.newBoolean(USE_OPTIONAL,
                "Use Optional container for optional parameters"));

        supportedLibraries.put(DEFAULT_LIBRARY, "Spring-boot Server application using the SpringFox integration.");
        supportedLibraries.put(SPRING_MVC_LIBRARY, "Spring-MVC Server application using the SpringFox integration.");
        supportedLibraries.put(SPRING_CLOUD_LIBRARY, "Spring-Cloud-Feign client with Spring-Boot auto-configured settings.");
        setLibrary(DEFAULT_LIBRARY);

        CliOption library = new CliOption(CodegenConstants.LIBRARY, "library template (sub-template) to use");
        library.setDefault(DEFAULT_LIBRARY);
        library.setEnum(supportedLibraries);
        library.setDefault(DEFAULT_LIBRARY);
        cliOptions.add(library);
        
    }

    /* (non-Javadoc)
     * @see io.swagger.codegen.languages.AbstractJavaCodegen#apiFileFolder()
     */
    @Override
    public String apiFileFolder() {
        return outputFolder + File.separator + sourceFolder;
    }

    /* (non-Javadoc)
     * @see io.swagger.codegen.CodegenConfig#getTag()
     */
    @Override
    public CodegenType getTag() {
        return CodegenType.SERVER;
    }

    /* (non-Javadoc)
     * @see io.swagger.codegen.CodegenConfig#getName()
     */
    @Override
    public String getName() {
        return "bian";
    }

    /* (non-Javadoc)
     * @see io.swagger.codegen.CodegenConfig#getHelp()
     */
    @Override
    public String getHelp() {
        return "Generates a Java SpringBoot Server application using the SpringFox integration.";
    }

    /* (non-Javadoc)
     * @see io.swagger.codegen.languages.AbstractJavaCodegen#processOpts()
     */
    @Override
    public void processOpts() {

        // Process java8 option before common java ones to change the default dateLibrary to java8.
        if (additionalProperties.containsKey(JAVA_8)) {
            this.setJava8(Boolean.valueOf(additionalProperties.get(JAVA_8).toString()));
        }
        if (this.java8) {
            additionalProperties.put("javaVersion", "1.8");
            additionalProperties.put("jdk8", "true");
            if (!additionalProperties.containsKey(DATE_LIBRARY)) {
                setDateLibrary("java8");
            }
        }

        // set invokerPackage as basePackage
        if (additionalProperties.containsKey(CodegenConstants.INVOKER_PACKAGE)) {
            this.setBasePackage((String) additionalProperties.get(CodegenConstants.INVOKER_PACKAGE));
            additionalProperties.put(BASE_PACKAGE, basePackage);
            LOGGER.info("Set base package to invoker package (" + basePackage + ")");
        }

        super.processOpts();

        // clear model and api doc template as this codegen
        // does not support auto-generated markdown doc at the moment
        //TODO: add doc templates
        modelDocTemplateFiles.remove("model_doc.mustache");
        apiDocTemplateFiles.remove("api_doc.mustache");

        if (additionalProperties.containsKey(TITLE)) {
            this.setTitle((String) additionalProperties.get(TITLE));
        }

        if (additionalProperties.containsKey(CONFIG_PACKAGE)) {
            this.setConfigPackage((String) additionalProperties.get(CONFIG_PACKAGE));
        }

        if (additionalProperties.containsKey(BASE_PACKAGE)) {
            this.setBasePackage((String) additionalProperties.get(BASE_PACKAGE));
        }

        if (additionalProperties.containsKey(INTERFACE_ONLY)) {
            this.setInterfaceOnly(Boolean.valueOf(additionalProperties.get(INTERFACE_ONLY).toString()));
        }

        if (additionalProperties.containsKey(DELEGATE_PATTERN)) {
            this.setDelegatePattern(Boolean.valueOf(additionalProperties.get(DELEGATE_PATTERN).toString()));
        }

        if (additionalProperties.containsKey(SINGLE_CONTENT_TYPES)) {
            this.setSingleContentTypes(Boolean.valueOf(additionalProperties.get(SINGLE_CONTENT_TYPES).toString()));
        }

        if (additionalProperties.containsKey(JAVA_8)) {
            this.setJava8(Boolean.valueOf(additionalProperties.get(JAVA_8).toString()));
        }

        if (additionalProperties.containsKey(ASYNC)) {
            this.setAsync(Boolean.valueOf(additionalProperties.get(ASYNC).toString()));
        }

        if (additionalProperties.containsKey(RESPONSE_WRAPPER)) {
            this.setResponseWrapper((String) additionalProperties.get(RESPONSE_WRAPPER));
        }

        if (additionalProperties.containsKey(USE_TAGS)) {
            this.setUseTags(Boolean.valueOf(additionalProperties.get(USE_TAGS).toString()));
        }
        
        if (additionalProperties.containsKey(USE_BEANVALIDATION)) {
            this.setUseBeanValidation(convertPropertyToBoolean(USE_BEANVALIDATION));
        }

        if (additionalProperties.containsKey(USE_OPTIONAL)) {
            this.setUseOptional(convertPropertyToBoolean(USE_OPTIONAL));
        }

        if (useBeanValidation) {
            writePropertyBack(USE_BEANVALIDATION, useBeanValidation);
        }

        if (additionalProperties.containsKey(IMPLICIT_HEADERS)) {
            this.setImplicitHeaders(Boolean.valueOf(additionalProperties.get(IMPLICIT_HEADERS).toString()));
        }

        if (additionalProperties.containsKey(SWAGGER_DOCKET_CONFIG)) {
            this.setSwaggerDocketConfig(Boolean.valueOf(additionalProperties.get(SWAGGER_DOCKET_CONFIG).toString()));
        }

        typeMapping.put("file", "Resource");
        importMapping.put("Resource", "org.springframework.core.io.Resource");
        
        if (useOptional) {
            writePropertyBack(USE_OPTIONAL, useOptional);
        }

        if (this.interfaceOnly && this.delegatePattern) {
            if (this.java8) {
                this.delegateMethod = true;
                additionalProperties.put("delegate-method", true);
            } else {
                throw new IllegalArgumentException(
                        String.format("Can not generate code with `%s` and `%s` true while `%s` is false.",
                                DELEGATE_PATTERN, INTERFACE_ONLY, JAVA_8));
            }
        }

//        supportingFiles.add(new SupportingFile("pom.mustache", "", "pom.xml"));
//        supportingFiles.add(new SupportingFile("README.mustache", "", "README.md"));

		if (!this.interfaceOnly) {
			apiTemplateFiles.put("service.mustache", "org/bian/service/{{apiPrefix}}Service.java");
			apiTemplateFiles.put("serviceImpl.mustache", "org/bian/service/{{apiPrefix}}ServiceImpl.java");
			apiTemplateFiles.put("apiController.mustache", "org/bian/controller/{{apiPrefix}}Controller.java");

			supportingFiles.add(new SupportingFile("gradle.mustache", "", "gradle.properties"));
			supportingFiles.add(new SupportingFile("gradlew.mustache", "", "gradlew"));
			supportingFiles.add(new SupportingFile("gradlew.bat.mustache", "", "gradlew.bat"));
			supportingFiles.add(new SupportingFile("build.mustache", "", "build.gradle"));
			supportingFiles.add(new SupportingFile("settings.mustache", "", "settings.gradle"));
			supportingFiles.add(new SupportingFile("Dockerfile.mustache", "", "Dockerfile"));

			supportingFiles.add(new SupportingFile("gradle-wrapper.properties.mustache", "gradle" + File.separator + "wrapper", "gradle-wrapper.properties"));
			supportingFiles.add(new SupportingFile("gradle-wrapper.jar.mustache", "gradle" + File.separator + "wrapper", "gradle-wrapper.jar"));

			supportingFiles.add(new SupportingFile("application.mustache",
					("src.main.resources").replace(".", java.io.File.separator), "application.properties"));

			supportingFiles.add(new SupportingFile("banner.mustache",
					("src.main.resources").replace(".", java.io.File.separator), "banner.txt"));
			
			supportingFiles.add(new SupportingFile("application.java.mustache",
					(sourceFolder + File.separator + basePackage).replace(".", java.io.File.separator), "Application.java"));

			supportingFiles.add(new SupportingFile("HealthController.mustache",
					("src.main.java." + controllerPackage).replace(".", java.io.File.separator), "HealthController.java"));

			supportingFiles.add(new SupportingFile("BQRequestMappingHandlerMapping.mustache",
					("src.main.java." + utilPackage).replace(".", java.io.File.separator), "BQRequestMappingHandlerMapping.java"));

			supportingFiles.add(new SupportingFile("WebMvcConfig.mustache",
					("src.main.java." + utilPackage).replace(".", java.io.File.separator), "WebMvcConfig.java"));

			supportingFiles.add(new SupportingFile("BianAnnotationHandler.mustache",
					("src.main.java." + utilPackage).replace(".", java.io.File.separator), "BianAnnotationHandler.java"));
	        
			// Helm
			supportingFiles.add(new SupportingFile("charts/Chart.yaml.mustache",
					"charts" + File.separator + "{{projName}}", "Chart.yaml"));
	        
			supportingFiles.add(new SupportingFile("charts/values.yaml.mustache",
					"charts" + File.separator + "{{projName}}", "values.yaml"));
	        
			supportingFiles.add(new SupportingFile("charts/helmignore.mustache",
					"charts" + File.separator + "{{projName}}", ".helmignore"));
	        
			supportingFiles.add(new SupportingFile("charts/templates/_helpers.tpl.mustache",
					"charts" + File.separator + "{{projName}}" + File.separator + "templates", "_helpers.tpl"));
	        
			supportingFiles.add(new SupportingFile("charts/templates/deployment.yaml.mustache",
					"charts" + File.separator + "{{projName}}" + File.separator + "templates", "deployment.yaml"));
	        
			supportingFiles.add(new SupportingFile("charts/templates/ingress.yaml.mustache",
					"charts" + File.separator + "{{projName}}" + File.separator + "templates", "ingress.yaml"));
	        
			supportingFiles.add(new SupportingFile("charts/templates/NOTES.txt.mustache",
					"charts" + File.separator + "{{projName}}" + File.separator + "templates", "NOTES.txt"));
	        
			supportingFiles.add(new SupportingFile("charts/templates/service.yaml.mustache",
					"charts" + File.separator + "{{projName}}" + File.separator + "templates", "service.yaml"));

		} else if ( this.swaggerDocketConfig && !library.equals(SPRING_CLOUD_LIBRARY)) {
            supportingFiles.add(new SupportingFile("swaggerDocumentationConfig.mustache",
                    (sourceFolder + File.separator + configPackage).replace(".", java.io.File.separator), "SwaggerDocumentationConfig.java"));
        }
        
        if ((!this.delegatePattern && this.java8) || this.delegateMethod) {
            additionalProperties.put("jdk8-no-delegate", true);
        }


        if (this.delegatePattern && !this.delegateMethod) {
            additionalProperties.put("isDelegate", "true");
            apiTemplateFiles.put("apiDelegate.mustache", "Delegate.java");
        }

        if (this.java8) {
            additionalProperties.put("javaVersion", "1.8");
            additionalProperties.put("jdk8", "true");
            if (this.async) {
                additionalProperties.put(RESPONSE_WRAPPER, "CompletableFuture");
            }
        } else if (this.async) {
            additionalProperties.put(RESPONSE_WRAPPER, "Callable");
        }

        // Some well-known Spring or Spring-Cloud response wrappers
        switch (this.responseWrapper) {
            case "Future":
            case "Callable":
            case "CompletableFuture":
                additionalProperties.put(RESPONSE_WRAPPER, "java.util.concurrent" + this.responseWrapper);
                break;
            case "ListenableFuture":
                additionalProperties.put(RESPONSE_WRAPPER, "org.springframework.util.concurrent.ListenableFuture");
                break;
            case "DeferredResult":
                additionalProperties.put(RESPONSE_WRAPPER, "org.springframework.web.context.request.async.DeferredResult");
                break;
            case "HystrixCommand":
                additionalProperties.put(RESPONSE_WRAPPER, "com.netflix.hystrix.HystrixCommand");
                break;
            case "RxObservable":
                additionalProperties.put(RESPONSE_WRAPPER, "rx.Observable");
                break;
            case "RxSingle":
                additionalProperties.put(RESPONSE_WRAPPER, "rx.Single");
                break;
            default:
                break;
        }

        // add lambda for mustache templates
        additionalProperties.put("lambdaEscapeDoubleQuote", new Mustache.Lambda() {
            @Override
            public void execute(Template.Fragment fragment, Writer writer) throws IOException {
                writer.write(fragment.execute().replaceAll("\"", Matcher.quoteReplacement("\\\"")));
            }
        });
        additionalProperties.put("lambdaRemoveLineBreak", new Mustache.Lambda() {
            @Override
            public void execute(Template.Fragment fragment, Writer writer) throws IOException {
                writer.write(fragment.execute().replaceAll("\\r|\\n", ""));
            }
        });
    }

    /* (non-Javadoc)
     * @see io.swagger.codegen.DefaultCodegen#addOperationToGroup(java.lang.String, java.lang.String, io.swagger.models.Operation, io.swagger.codegen.CodegenOperation, java.util.Map)
     */
    @Override
    public void addOperationToGroup(String tag, String resourcePath, Operation operation, CodegenOperation co, Map<String, List<CodegenOperation>> operations) {
        if((library.equals(DEFAULT_LIBRARY) || library.equals(SPRING_MVC_LIBRARY)) && !useTags) {
            String basePath = resourcePath;
            if (basePath.startsWith("/")) {
                basePath = basePath.substring(1);
            }
            int pos = basePath.indexOf("/");
            if (pos > 0) {
                basePath = basePath.substring(0, pos);
            }

            if (basePath.equals("")) {
                basePath = "default";
            } else {
                co.subresourceOperation = !co.path.isEmpty();
            }
            List<CodegenOperation> opList = operations.get(basePath);
            if (opList == null) {
                opList = new ArrayList<CodegenOperation>();
                operations.put(basePath, opList);
            }
            opList.add(co);
            co.baseName = basePath;
        } else {
            super.addOperationToGroup(tag, resourcePath, operation, co, operations);
        }
    }

    /* (non-Javadoc)
     * @see io.swagger.codegen.languages.AbstractJavaCodegen#preprocessSwagger(io.swagger.models.Swagger)
     */
    @Override
    public void preprocessSwagger(Swagger swagger) {
        super.preprocessSwagger(swagger);
        if ("/".equals(swagger.getBasePath())) {
            swagger.setBasePath("");
        }

        if(!additionalProperties.containsKey(TITLE)) {
            // From the title, compute a reasonable name for the package and the API
            String title = swagger.getInfo().getTitle();

            // Drop any API suffix
            if (title != null) {
                title = title.trim().replace(" ", "-");
                if (title.toUpperCase().endsWith("API")) {
                    title = title.substring(0, title.length() - 3);
                }

                this.title = camelize(sanitizeName(title), true);
            }
            additionalProperties.put(TITLE, this.title);
        }

        String host = swagger.getHost();
        String port = "8080";
        if (host != null) {
            String[] parts = host.split(":");
            if (parts.length > 1) {
                port = parts[1];
            }
        }

        this.additionalProperties.put("serverPort", port);
        if (swagger.getPaths() != null) {
            for (String pathname : swagger.getPaths().keySet()) {
                Path path = swagger.getPath(pathname);
                if (path.getOperations() != null) {
                    for (Operation operation : path.getOperations()) {
                        if (operation.getTags() != null) {
                            List<Map<String, String>> tags = new ArrayList<Map<String, String>>();
                            for (String tag : operation.getTags()) {
                                Map<String, String> value = new HashMap<String, String>();
                                value.put("tag", tag);
                                value.put("hasMore", "true");
                                tags.add(value);
                            }
                            if (tags.size() > 0) {
                                tags.get(tags.size() - 1).remove("hasMore");
                            }
                            if (operation.getTags().size() > 0) {
                                String tag = operation.getTags().get(0);
                                operation.setTags(Arrays.asList(tag));
                            }
                            operation.setVendorExtension("x-tags", tags);
                        }
                    }
                }
            }
        }
    }

    /* (non-Javadoc)
     * @see io.swagger.codegen.languages.AbstractJavaCodegen#postProcessOperations(java.util.Map)
     */
    @Override
    public Map<String, Object> postProcessOperations(Map<String, Object> objs) {
        Map<String, Object> operations = (Map<String, Object>) objs.get("operations");
        if (operations != null) {
            List<CodegenOperation> ops = (List<CodegenOperation>) operations.get("operation");
            for (final CodegenOperation operation : ops) {
                List<CodegenResponse> responses = operation.responses;
                if (responses != null) {
                    for (final CodegenResponse resp : responses) {
                        if ("0".equals(resp.code)) {
                            resp.code = "200";
                        }
                        doDataTypeAssignment(resp.dataType, new DataTypeAssigner() {
                            @Override
                            public void setReturnType(final String returnType) {
                                resp.dataType = returnType;
                            }

                            @Override
                            public void setReturnContainer(final String returnContainer) {
                                resp.containerType = returnContainer;
                            }
                        });
                    }
                }

                doDataTypeAssignment(operation.returnType, new DataTypeAssigner() {

                    @Override
                    public void setReturnType(final String returnType) {
                        operation.returnType = returnType;
                    }

                    @Override
                    public void setReturnContainer(final String returnContainer) {
                        operation.returnContainer = returnContainer;
                    }
                });

                if(implicitHeaders){
                    removeHeadersFromAllParams(operation.allParams);
                }
            }
        }

        return objs;
    }

    /**
     * The Interface DataTypeAssigner.
     */
    private interface DataTypeAssigner {
        
        /**
         * Sets the return type.
         *
         * @param returnType the new return type
         */
        void setReturnType(String returnType);
        
        /**
         * Sets the return container.
         *
         * @param returnContainer the new return container
         */
        void setReturnContainer(String returnContainer);
    }

    /**
     * Do data type assignment.
     *
     * @param returnType The return type that needs to be converted
     * @param dataTypeAssigner An object that will assign the data to the respective fields in the model.
     */
    private void doDataTypeAssignment(String returnType, DataTypeAssigner dataTypeAssigner) {
        final String rt = returnType;
        if (rt == null) {
            dataTypeAssigner.setReturnType("Void");
        } else if (rt.startsWith("List")) {
            int end = rt.lastIndexOf(">");
            if (end > 0) {
                dataTypeAssigner.setReturnType(rt.substring("List<".length(), end).trim());
                dataTypeAssigner.setReturnContainer("List");
            }
        } else if (rt.startsWith("Map")) {
            int end = rt.lastIndexOf(">");
            if (end > 0) {
                dataTypeAssigner.setReturnType(rt.substring("Map<".length(), end).split(",")[1].trim());
                dataTypeAssigner.setReturnContainer("Map");
            }
        } else if (rt.startsWith("Set")) {
            int end = rt.lastIndexOf(">");
            if (end > 0) {
                dataTypeAssigner.setReturnType(rt.substring("Set<".length(), end).trim());
                dataTypeAssigner.setReturnContainer("Set");
            }
        }
    }

    /**
     * This method removes header parameters from the list of parameters and also
     * corrects last allParams hasMore state.
     * @param allParams list of all parameters
     */
    private void removeHeadersFromAllParams(List<CodegenParameter> allParams) {
        if(allParams.isEmpty()){
            return;
        }
        final ArrayList<CodegenParameter> copy = new ArrayList<>(allParams);
        allParams.clear();

        for(CodegenParameter p : copy){
            if(!p.isHeaderParam){
                allParams.add(p);
            }
        }
        allParams.get(allParams.size()-1).hasMore =false;
    }

    /* (non-Javadoc)
     * @see io.swagger.codegen.DefaultCodegen#postProcessSupportingFileData(java.util.Map)
     */
    @Override
    public Map<String, Object> postProcessSupportingFileData(Map<String, Object> objs) {
        if(library.equals(SPRING_CLOUD_LIBRARY)) {
            List<CodegenSecurity> authMethods = (List<CodegenSecurity>) objs.get("authMethods");
            if (authMethods != null) {
                for (CodegenSecurity authMethod : authMethods) {
                    authMethod.name = camelize(sanitizeName(authMethod.name), true);
                }
            }
        }
        return objs;
    }

    /* (non-Javadoc)
     * @see io.swagger.codegen.languages.AbstractJavaCodegen#toApiName(java.lang.String)
     */
    @Override
    public String toApiName(String name) {
        if (name.length() == 0) {
            return "DefaultApi";
        }
        name = sanitizeName(name);
        return camelize(name) + "Api";
    }

    /* (non-Javadoc)
     * @see io.swagger.codegen.languages.AbstractJavaCodegen#setParameterExampleValue(io.swagger.codegen.CodegenParameter)
     */
    @Override
    public void setParameterExampleValue(CodegenParameter p) {
        String type = p.baseType;
        if (type == null) {
            type = p.dataType;
        }

        if ("File".equals(type)) {
            String example;

            if (p.defaultValue == null) {
                example = p.example;
            } else {
                example = p.defaultValue;
            }

            if (example == null) {
                example = "/path/to/file";
            }
            example = "new org.springframework.core.io.FileSystemResource(new java.io.File(\"" + escapeText(example) + "\"))";
            p.example = example;
        } else {
            super.setParameterExampleValue(p);
        }
    }

    /**
     * Sets the title.
     *
     * @param title the new title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Sets the config package.
     *
     * @param configPackage the new config package
     */
    public void setConfigPackage(String configPackage) {
        this.configPackage = configPackage;
    }

    /**
     * Sets the base package.
     *
     * @param configPackage the new base package
     */
    public void setBasePackage(String configPackage) {
        this.basePackage = configPackage;
    }

    /**
     * Sets the interface only.
     *
     * @param interfaceOnly the new interface only
     */
    public void setInterfaceOnly(boolean interfaceOnly) { this.interfaceOnly = interfaceOnly; }

    /**
     * Sets the delegate pattern.
     *
     * @param delegatePattern the new delegate pattern
     */
    public void setDelegatePattern(boolean delegatePattern) { this.delegatePattern = delegatePattern; }

    /**
     * Sets the single content types.
     *
     * @param singleContentTypes the new single content types
     */
    public void setSingleContentTypes(boolean singleContentTypes) {
        this.singleContentTypes = singleContentTypes;
    }

    /**
     * Sets the java 8.
     *
     * @param java8 the new java 8
     */
    public void setJava8(boolean java8) { this.java8 = java8; }

    /**
     * Sets the async.
     *
     * @param async the new async
     */
    public void setAsync(boolean async) { this.async = async; }

    /**
     * Sets the response wrapper.
     *
     * @param responseWrapper the new response wrapper
     */
    public void setResponseWrapper(String responseWrapper) { this.responseWrapper = responseWrapper; }

    /**
     * Sets the use tags.
     *
     * @param useTags the new use tags
     */
    public void setUseTags(boolean useTags) {
        this.useTags = useTags;
    }

    /**
     * Sets the implicit headers.
     *
     * @param implicitHeaders the new implicit headers
     */
    public void setImplicitHeaders(boolean implicitHeaders) {
        this.implicitHeaders = implicitHeaders;
    }

    /**
     * Sets the swagger docket config.
     *
     * @param swaggerDocketConfig the new swagger docket config
     */
    public void setSwaggerDocketConfig(boolean swaggerDocketConfig) {
        this.swaggerDocketConfig = swaggerDocketConfig;
    }

    /* (non-Javadoc)
     * @see io.swagger.codegen.languages.AbstractJavaCodegen#postProcessModelProperty(io.swagger.codegen.CodegenModel, io.swagger.codegen.CodegenProperty)
     */
    @Override
    public void postProcessModelProperty(CodegenModel model, CodegenProperty property) {
        super.postProcessModelProperty(model, property);

        if ("null".equals(property.example)) {
            property.example = null;
        }

        //Add imports for Jackson
        if (!Boolean.TRUE.equals(model.isEnum)) {
            model.imports.add("JsonProperty");

            if (Boolean.TRUE.equals(model.hasEnums)) {
                model.imports.add("JsonValue");
            }
        } else { // enum class
            //Needed imports for Jackson's JsonCreator
            if (additionalProperties.containsKey("jackson")) {
                model.imports.add("JsonCreator");
            }
        }
    }

    /* (non-Javadoc)
     * @see io.swagger.codegen.DefaultCodegen#postProcessModelsEnum(java.util.Map)
     */
    @Override
    public Map<String, Object> postProcessModelsEnum(Map<String, Object> objs) {
        objs = super.postProcessModelsEnum(objs);

        //Add imports for Jackson
        List<Map<String, String>> imports = (List<Map<String, String>>)objs.get("imports");
        List<Object> models = (List<Object>) objs.get("models");
        for (Object _mo : models) {
            Map<String, Object> mo = (Map<String, Object>) _mo;
            CodegenModel cm = (CodegenModel) mo.get("model");
            // for enum model
            if (Boolean.TRUE.equals(cm.isEnum) && cm.allowableValues != null) {
                cm.imports.add(importMapping.get("JsonValue"));
                Map<String, String> item = new HashMap<String, String>();
                item.put("import", importMapping.get("JsonValue"));
                imports.add(item);
            }
        }

        return objs;
    }
    
    /* (non-Javadoc)
     * @see io.swagger.codegen.languages.features.BeanValidationFeatures#setUseBeanValidation(boolean)
     */
    public void setUseBeanValidation(boolean useBeanValidation) {
        this.useBeanValidation = useBeanValidation;
    }

    /* (non-Javadoc)
     * @see io.swagger.codegen.languages.features.OptionalFeatures#setUseOptional(boolean)
     */
    @Override
    public void setUseOptional(boolean useOptional) {
        this.useOptional = useOptional;
    }
    
    /* (non-Javadoc)
     * @see io.swagger.codegen.languages.AbstractJavaCodegen#fromOperation(java.lang.String, java.lang.String, io.swagger.models.Operation, java.util.Map, io.swagger.models.Swagger)
     */
    @Override
    public CodegenOperation fromOperation(String path, String httpMethod, Operation operation, Map<String, Model> definitions, Swagger swagger) {
        CodegenOperation op = super.fromOperation(path, httpMethod, operation, definitions, swagger);
        this.generateActionTerm(op);
        String appName = additionalProperties.get("appName").toString();
        String projName = appName.replaceAll(" - ", "-").replaceAll(" ", "-").toLowerCase();
        additionalProperties.put("projName", projName);
        return op;
    }
    
	/**
	 * Generate json example data.
	 *
	 * @param op the op
	 */
	private void generateJsonExampleData(CodegenOperation op, String actionTerm) {
		if (op.examples != null && op.examples.size() > 0) {
			String fileName = actionTerm + "-" + op.returnBaseType + ".json";
			additionalProperties.put(fileName, op.examples.get(0).get("example").replaceAll("\"\\{\\}\"", "{}"));
			supportingFiles.add(new SupportingFile("json.mustache",
					("src.main.resources").replace(".", java.io.File.separator), fileName));
		}
		// apiTemplateFiles.put("json.mustache", filenName);

	}
    
    /**
     * Generate action term.
     *
     * @param op the op
     */
    private void generateActionTerm(CodegenOperation op) {
		ServiceOperation serviceOperation = this.resolveServiceOperation(op.path, op.httpMethod);
		if(serviceOperation == null) {
			LOGGER.error("Unable to map an Action Term for the path: " + op.path);
		} else {
			String actionTermCamelCase = WordUtils.uncapitalize(serviceOperation.getActionTerm());
	
			Map<String, Boolean> actionTerms = new HashMap<String, Boolean>();
			actionTerms.put(actionTermCamelCase, true);
	
			op.vendorExtensions.put("actionTermCamelCase", actionTermCamelCase);
			op.vendorExtensions.put("actionTermTitleCase", StringUtils.capitalize(actionTermCamelCase));
			op.vendorExtensions.put("actionTerms", actionTerms);
			
			this.generateJsonExampleData(op, actionTermCamelCase);
			
			if (serviceOperation.getBehavioralQualifier() != null) {
				op.vendorExtensions.put("behavioralQualifier", serviceOperation.getBehavioralQualifier());
				op.vendorExtensions.put("behavioralQualifierTitleCase",
						Arrays.stream(serviceOperation.getBehavioralQualifier().split("\\-")).map(String::toLowerCase)
								.map(s -> s.substring(0, 1).toUpperCase() + s.substring(1))
								.collect(Collectors.joining()));
			}
		}
    }
    
    /**
     * Resolve service operation.
     *
     * @param url the url
     * @param httpMethod the http method
     * @return the service operation
     */
    private ServiceOperation resolveServiceOperation(String url, String httpMethod) {
    	
        String[] urlChunks = StringUtils.split(url, "/");
        ServiceOperation serviceOperation = new ServiceOperation();

        this.resolveServiceDomain(urlChunks, serviceOperation);
		this.resolveActionTerm(urlChunks, httpMethod, serviceOperation);
		this.resolveFunctionalPattern(serviceOperation);

		this.setGlobalAdditionalProperty("serviceDomain", serviceOperation.getServiceDomain());
		this.setGlobalAdditionalProperty("controlRecord", serviceOperation.getControlRecord());
		this.setGlobalAdditionalProperty("functionalPattern", serviceOperation.getFunctionalParttern());
		
		return serviceOperation;
    }
    
	/**
	 * Resolve service domain.
	 *
	 * @param urlChunks the url chunks
	 * @param serviceOperation the service operation
	 */
	private void resolveServiceDomain(String[] urlChunks, ServiceOperation serviceOperation) {
		if (serviceOperation == null) {
			serviceOperation = new ServiceOperation();
		}
		if (urlChunks != null && urlChunks.length > 0) {
			serviceOperation.setServiceDomain(urlChunks[0]);
		} else {
			LOGGER.error("No Service Domain found. Probably a malformed request.");
		}
	}
    
	/**
	 * Resolve control record.
	 *
	 * @param urlChunks the url chunks
	 * @param serviceOperation the service operation
	 */
	private void resolveFunctionalPattern(ServiceOperation serviceOperation) {
		String controlRecord = serviceOperation.getControlRecord();
		if(controlRecord != null) {
			if (StringUtils.endsWith(controlRecord.toLowerCase(), "strategy")) {
				serviceOperation.setFunctionalParttern("Direct");
			} else if (StringUtils.endsWith(controlRecord.toLowerCase(), "management-plan") || StringUtils.endsWith(controlRecord.toLowerCase(), "managementplan")) {
				serviceOperation.setFunctionalParttern("Manage");
			} else if (StringUtils.endsWith(controlRecord.toLowerCase(), "administrative-plan") || StringUtils.endsWith(controlRecord.toLowerCase(), "administrativeplan")) {
				serviceOperation.setFunctionalParttern("Administer");
			} else if (StringUtils.endsWith(controlRecord.toLowerCase(), "specification")) {
				serviceOperation.setFunctionalParttern("Design");
			} else if (StringUtils.endsWith(controlRecord.toLowerCase(), "development-project")) {
				serviceOperation.setFunctionalParttern("Develop");
			} else if (StringUtils.endsWith(controlRecord.toLowerCase(), "procedure")) {
				serviceOperation.setFunctionalParttern("Process");
			} else if (StringUtils.endsWith(controlRecord.toLowerCase(), "operating-session") || StringUtils.endsWith(controlRecord.toLowerCase(), "atmnetworkoperatingsession")) {
				serviceOperation.setFunctionalParttern("Operate");
			} else if (StringUtils.endsWith(controlRecord.toLowerCase(), "maintenance-agreement")) {
				serviceOperation.setFunctionalParttern("Maintain");
			} else if (StringUtils.endsWith(controlRecord.toLowerCase(), "fulfillment-arrangement")) {
				serviceOperation.setFunctionalParttern("Fulfill");
			} else if (StringUtils.endsWith(controlRecord.toLowerCase(), "arrangement")) {
                serviceOperation.setFunctionalParttern("Fulfill");
            } else if (StringUtils.endsWith(controlRecord.toLowerCase(), "transaction")) {
				serviceOperation.setFunctionalParttern("Transact");
			} else if (StringUtils.endsWith(controlRecord.toLowerCase(), "advise")) {
				serviceOperation.setFunctionalParttern("Advise");
			} else if (StringUtils.endsWith(controlRecord.toLowerCase(), "state")) {
				serviceOperation.setFunctionalParttern("Monitor");
			} else if (StringUtils.endsWith(controlRecord.toLowerCase(), "log")) {
				serviceOperation.setFunctionalParttern("Track");
			} else if (StringUtils.endsWith(controlRecord.toLowerCase(), "directory-entry")) {
				serviceOperation.setFunctionalParttern("Catalog");
			} else if (StringUtils.endsWith(controlRecord.toLowerCase(), "membership")) {
				serviceOperation.setFunctionalParttern("Enroll");
			} else if (StringUtils.endsWith(controlRecord.toLowerCase(), "agreement")) {
				serviceOperation.setFunctionalParttern("AgreeTerms");
			} else if (StringUtils.endsWith(controlRecord.toLowerCase(), "assessment")) {
				serviceOperation.setFunctionalParttern("Assess");
			} else if (StringUtils.endsWith(controlRecord.toLowerCase(), "analysis")) {
				serviceOperation.setFunctionalParttern("Analyse");
			} else if (StringUtils.endsWith(controlRecord.toLowerCase(), "allocation")) {
				serviceOperation.setFunctionalParttern("Allocate");
			} else {
				LOGGER.error("Invalid control record. Couldn't generate a Functional Pattern");
			}
		}
	}
    
    /**
     * Resolve action term.
     *
     * @param urlChunks the url chunks
     * @param httpMethod the http method
     * @param serviceOperation the service operation
     */
    private void resolveActionTerm(String[] urlChunks, String httpMethod, ServiceOperation serviceOperation) {
    	if(serviceOperation == null) {
    		serviceOperation = new ServiceOperation();
    	}
    	
		if ("initiation".equalsIgnoreCase(urlChunks[urlChunks.length - 1]) && "POST".equalsIgnoreCase(httpMethod)) {
			serviceOperation.setActionTerm("initiate");
			serviceOperation.setControlRecord(urlChunks[2]);
			if (urlChunks.length >= 6) {
				serviceOperation.setBehavioralQualifier(urlChunks[urlChunks.length - 2]);
			}
		} else if ("creation".equalsIgnoreCase(urlChunks[urlChunks.length - 1]) && "POST".equalsIgnoreCase(httpMethod)) {
			serviceOperation.setActionTerm("create");
			serviceOperation.setControlRecord(urlChunks[2]);
			if (urlChunks.length >= 6) {
				serviceOperation.setBehavioralQualifier(urlChunks[urlChunks.length - 2]);
			}
		} else if ("activation".equalsIgnoreCase(urlChunks[urlChunks.length - 1]) && "POST".equalsIgnoreCase(httpMethod)) {
			serviceOperation.setActionTerm("activate");
		} else if ("registration".equalsIgnoreCase(urlChunks[urlChunks.length - 1]) && "POST".equalsIgnoreCase(httpMethod)) {
			serviceOperation.setActionTerm("register");
			serviceOperation.setControlRecord(urlChunks[2]);
			if (urlChunks.length >= 6) {
				serviceOperation.setBehavioralQualifier(urlChunks[urlChunks.length - 2]);
			}
		} else if ("configuration".equalsIgnoreCase(urlChunks[urlChunks.length - 1]) && "PUT".equalsIgnoreCase(httpMethod)) {
			serviceOperation.setActionTerm("configure");
		} else if ("feedback".equalsIgnoreCase(urlChunks[urlChunks.length - 1]) && "PUT".equalsIgnoreCase(httpMethod)) {
			serviceOperation.setActionTerm("feedback");
		} else if ("update".equalsIgnoreCase(urlChunks[urlChunks.length - 1]) && "PUT".equalsIgnoreCase(httpMethod)) {
			serviceOperation.setActionTerm("update");
			serviceOperation.setControlRecord(urlChunks[2]);
			if (urlChunks.length >= 7) {
				serviceOperation.setBehavioralQualifier(urlChunks[urlChunks.length - 3]);
			}
		} else if ("control".equalsIgnoreCase(urlChunks[urlChunks.length - 1]) && "PUT".equalsIgnoreCase(httpMethod)) {
			serviceOperation.setActionTerm("control");
			serviceOperation.setControlRecord(urlChunks[2]);
			if (urlChunks.length >= 7) {
				serviceOperation.setBehavioralQualifier(urlChunks[urlChunks.length - 3]);
			}
		} else if ("exchange".equalsIgnoreCase(urlChunks[urlChunks.length - 1]) && "PUT".equalsIgnoreCase(httpMethod)) {
			serviceOperation.setActionTerm("exchange");
			serviceOperation.setControlRecord(urlChunks[2]);
			if (urlChunks.length >= 7) {
				serviceOperation.setBehavioralQualifier(urlChunks[urlChunks.length - 3]);
			}
		} else if ("capture".equalsIgnoreCase(urlChunks[urlChunks.length - 1]) && "PUT".equalsIgnoreCase(httpMethod)) {
			serviceOperation.setActionTerm("capture");
			serviceOperation.setControlRecord(urlChunks[2]);
			if (urlChunks.length >= 7) {
				serviceOperation.setBehavioralQualifier(urlChunks[urlChunks.length - 3]);
			}
		} else if ("recording".equalsIgnoreCase(urlChunks[urlChunks.length - 1]) && "POST".equalsIgnoreCase(httpMethod)) {
			serviceOperation.setActionTerm("record");
			if (urlChunks.length >= 6) {
				serviceOperation.setBehavioralQualifier(urlChunks[urlChunks.length - 3]);
			}
		} else if ("execution".equalsIgnoreCase(urlChunks[urlChunks.length - 1]) && "PUT".equalsIgnoreCase(httpMethod)) {
			serviceOperation.setActionTerm("execute");
			serviceOperation.setControlRecord(urlChunks[2]);
			if (urlChunks.length == 7) {
				serviceOperation.setBehavioralQualifier(urlChunks[urlChunks.length - 3]);
			}
		} else if ("evaluation".equalsIgnoreCase(urlChunks[urlChunks.length - 1]) && "POST".equalsIgnoreCase(httpMethod)) {
			serviceOperation.setActionTerm("evaluate");
			serviceOperation.setControlRecord(urlChunks[2]);
			if (urlChunks.length >= 6) {
				serviceOperation.setBehavioralQualifier(urlChunks[urlChunks.length - 2]);
			}
		} else if ("provision".equalsIgnoreCase(urlChunks[urlChunks.length - 1]) && "POST".equalsIgnoreCase(httpMethod)) {
			serviceOperation.setActionTerm("provide");
			serviceOperation.setControlRecord(urlChunks[2]);
			if (urlChunks.length >= 6) {
				serviceOperation.setBehavioralQualifier(urlChunks[urlChunks.length - 2]);
			}
		} else if ("authorization".equalsIgnoreCase(urlChunks[urlChunks.length - 1]) && "POST".equalsIgnoreCase(httpMethod)) {
			serviceOperation.setActionTerm("authorize");
			if (urlChunks.length >= 5) {
				serviceOperation.setBehavioralQualifier(urlChunks[urlChunks.length - 2]);
			}
		} else if ("requisition".equalsIgnoreCase(urlChunks[urlChunks.length - 1]) && "PUT".equalsIgnoreCase(httpMethod)) {
			serviceOperation.setActionTerm("request");
			serviceOperation.setControlRecord(urlChunks[2]);
			if (urlChunks.length == 7) {
				serviceOperation.setBehavioralQualifier(urlChunks[urlChunks.length - 3]);
			}
		} else if ("grant".equalsIgnoreCase(urlChunks[urlChunks.length - 1]) && "PUT".equalsIgnoreCase(httpMethod)) {
			serviceOperation.setActionTerm("grant");
			serviceOperation.setControlRecord(urlChunks[2]);
			if (urlChunks.length == 7) {
				serviceOperation.setBehavioralQualifier(urlChunks[urlChunks.length - 3]);
			}
		} else if ("notification".equalsIgnoreCase(urlChunks[urlChunks.length - 1]) && "POST".equalsIgnoreCase(httpMethod)) {
			serviceOperation.setActionTerm("notify");
			serviceOperation.setControlRecord(urlChunks[2]);
			if (urlChunks.length >= 7) {
				serviceOperation.setBehavioralQualifier(urlChunks[urlChunks.length - 3]);
			}
		} else if ("DELETE".equalsIgnoreCase(httpMethod)) {
			serviceOperation.setActionTerm("terminate");
			if (urlChunks.length >= 5) {
				serviceOperation.setBehavioralQualifier(urlChunks[urlChunks.length - 2]);
			}
		} else if ("GET".equalsIgnoreCase(httpMethod)) {
			if (urlChunks.length == 2) {
				serviceOperation.setActionTerm("retrieveSD");
			}
			else if (urlChunks.length == 3 && "behavior-qualifiers".equals(urlChunks[2])) {
				serviceOperation.setActionTerm("retrieveBQs");
				serviceOperation.setControlRecord(urlChunks[1]);
			}
			else if (urlChunks.length == 3) {
				serviceOperation.setActionTerm("retrieveRefIds");
				serviceOperation.setControlRecord(urlChunks[2]);
			}
			else if (urlChunks.length == 4) {
				serviceOperation.setActionTerm("retrieve");
				serviceOperation.setControlRecord(urlChunks[2]);
			}
			else if (urlChunks.length == 5) {
				serviceOperation.setActionTerm("retrieveBQIds");
				serviceOperation.setControlRecord(urlChunks[2]);
			}
			else if (urlChunks.length == 6) {
				serviceOperation.setActionTerm("retrieve");
				serviceOperation.setControlRecord(urlChunks[2]);
				serviceOperation.setBehavioralQualifier(urlChunks[urlChunks.length - 2]);
			}
		} else {
			LOGGER.error("No valid action term found. Probably a malformed request.");
		}
    }
    
    /**
     * Sets the global additional property.
     *
     * @param key the key
     * @param value the value
     */
    private void setGlobalAdditionalProperty(String key, String value) {
		Object previousValue = additionalProperties.get(key);
		if (previousValue != null && !previousValue.equals(value)) {
			LOGGER.error(key + " is already set as '" + previousValue + "'. A new value '" + value
					+ "' is declared and is not allowed.");
			LOGGER.error("Continuing with the Control Record " + previousValue);
		} else {
			additionalProperties.put(key, value);
		}
    }
    
	/**
	 * The Class ServiceOperation.
	 */
	private class ServiceOperation {

		/** The service domain. */
		private String serviceDomain;
		
		/** The control record. */
		private String controlRecord;
		
		/** The functional parttern. */
		private String functionalParttern;
		
		/** The action term. */
		private String actionTerm;
		
		/** The behavioral qualifier. */
		private String behavioralQualifier;
		
		/**
		 * Gets the service domain.
		 *
		 * @return the service domain
		 */
		public String getServiceDomain() {
			return serviceDomain;
		}

		/**
		 * Sets the service domain.
		 *
		 * @param serviceDomain the new service domain
		 */
		public void setServiceDomain(String serviceDomain) {
			this.serviceDomain = serviceDomain;
		}

		/**
		 * Gets the control record.
		 *
		 * @return the control record
		 */
		public String getControlRecord() {
			return controlRecord;
		}

		/**
		 * Sets the control record.
		 *
		 * @param controlRecord the new control record
		 */
		public void setControlRecord(String controlRecord) {
			this.controlRecord = controlRecord;
		}

		/**
		 * Gets the functional parttern.
		 *
		 * @return the functional parttern
		 */
		public String getFunctionalParttern() {
			return functionalParttern;
		}

		/**
		 * Sets the functional parttern.
		 *
		 * @param functionalParttern the new functional parttern
		 */
		public void setFunctionalParttern(String functionalParttern) {
			this.functionalParttern = functionalParttern;
		}
		
		/**
		 * Gets the action term.
		 *
		 * @return the action term
		 */
		public String getActionTerm() {
			return actionTerm;
		}
		
		/**
		 * Sets the action term.
		 *
		 * @param actionTerm the new action term
		 */
		public void setActionTerm(String actionTerm) {
			this.actionTerm = actionTerm;
		}
		
		/**
		 * Gets the behavioral qualifier.
		 *
		 * @return the behavioral qualifier
		 */
		public String getBehavioralQualifier() {
			return behavioralQualifier;
		}
		
		/**
		 * Sets the behavioral qualifier.
		 *
		 * @param behavioralQualifier the new behavioral qualifier
		 */
		public void setBehavioralQualifier(String behavioralQualifier) {
			this.behavioralQualifier = behavioralQualifier;
		}
		
	}
}