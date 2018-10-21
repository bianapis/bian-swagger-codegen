package org.bian.swaggercodegen.config;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.Validate;
import org.bian.swaggercodegen.generator.JavaBianCodegen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

import io.swagger.codegen.CliOption;
import io.swagger.codegen.ClientOptInput;
import io.swagger.codegen.ClientOpts;
import io.swagger.codegen.CodegenConfig;
import io.swagger.codegen.CodegenConstants;
import io.swagger.codegen.auth.AuthParser;
import io.swagger.models.Swagger;
import io.swagger.models.auth.AuthorizationValue;
import io.swagger.parser.SwaggerParser;
import io.swagger.util.Json;

// TODO: Auto-generated Javadoc
/**
 * A class that contains all codegen configuration properties a user would want to manipulate.
 * An instance could be created by deserializing a JSON file or being populated from CLI or Maven plugin parameters.
 * It also has a convenience method for creating a ClientOptInput class which is THE object DefaultGenerator.java needs
 * to generate code.
 */
public class CodegenConfigurator implements Serializable {

    /** The Constant LOGGER. */
    public static final Logger LOGGER = LoggerFactory.getLogger(CodegenConfigurator.class);

    /** The lang. */
    private String lang;
    
    /** The input spec. */
    private String inputSpec;
    
    /** The output dir. */
    private String outputDir;
    
    /** The verbose. */
    private boolean verbose;
    
    /** The skip overwrite. */
    private boolean skipOverwrite;
    
    /** The remove operation id prefix. */
    private boolean removeOperationIdPrefix;
    
    /** The template dir. */
    private String templateDir;
    
    /** The auth. */
    private String auth;
    
    /** The api package. */
    private String apiPackage;
    
    /** The model package. */
    private String modelPackage;
    
    /** The invoker package. */
    private String invokerPackage;
    
    /** The model name prefix. */
    private String modelNamePrefix;
    
    /** The model name suffix. */
    private String modelNameSuffix;
    
    /** The group id. */
    private String groupId;
    
    /** The artifact id. */
    private String artifactId;
    
    /** The artifact version. */
    private String artifactVersion;
    
    /** The library. */
    private String library;
    
    /** The ignore file override. */
    private String ignoreFileOverride;
    
    /** The system properties. */
    private Map<String, String> systemProperties = new HashMap<String, String>();
    
    /** The instantiation types. */
    private Map<String, String> instantiationTypes = new HashMap<String, String>();
    
    /** The type mappings. */
    private Map<String, String> typeMappings = new HashMap<String, String>();
    
    /** The additional properties. */
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    
    /** The import mappings. */
    private Map<String, String> importMappings = new HashMap<String, String>();
    
    /** The language specific primitives. */
    private Set<String> languageSpecificPrimitives = new HashSet<String>();
    
    /** The reserved word mappings. */
    private Map<String, String>  reservedWordMappings = new HashMap<String, String>();

    /** The git user id. */
    private String gitUserId="GIT_USER_ID";
    
    /** The git repo id. */
    private String gitRepoId="GIT_REPO_ID";
    
    /** The release note. */
    private String releaseNote="Minor update";
    
    /** The http user agent. */
    private String httpUserAgent;

    /** The dynamic properties. */
    private final Map<String, Object> dynamicProperties = new HashMap<String, Object>(); //the map that holds the JsonAnySetter/JsonAnyGetter values

    /**
     * Instantiates a new codegen configurator.
     */
    public CodegenConfigurator() {
        this.setOutputDir(".");
    }

    /**
     * Sets the lang.
     *
     * @param lang the lang
     * @return the codegen configurator
     */
    public CodegenConfigurator setLang(String lang) {
        this.lang = lang;
        return this;
    }

    /**
     * Sets the input spec.
     *
     * @param inputSpec the input spec
     * @return the codegen configurator
     */
    public CodegenConfigurator setInputSpec(String inputSpec) {
        this.inputSpec = inputSpec;
        return this;
    }

    /**
     * Gets the input spec.
     *
     * @return the input spec
     */
    public String getInputSpec() {
        return inputSpec;
    }

    /**
     * Gets the output dir.
     *
     * @return the output dir
     */
    public String getOutputDir() {
        return outputDir;
    }

    /**
     * Sets the output dir.
     *
     * @param outputDir the output dir
     * @return the codegen configurator
     */
    public CodegenConfigurator setOutputDir(String outputDir) {
        this.outputDir = toAbsolutePathStr(outputDir);
        return this;
    }

    /**
     * Gets the model package.
     *
     * @return the model package
     */
    public String getModelPackage() {
        return modelPackage;
    }

    /**
     * Sets the model package.
     *
     * @param modelPackage the model package
     * @return the codegen configurator
     */
    public CodegenConfigurator setModelPackage(String modelPackage) {
        this.modelPackage = modelPackage;
        return this;
    }

    /**
     * Gets the model name prefix.
     *
     * @return the model name prefix
     */
    public String getModelNamePrefix() {
        return modelNamePrefix;
    }

    /**
     * Sets the model name prefix.
     *
     * @param prefix the prefix
     * @return the codegen configurator
     */
    public CodegenConfigurator setModelNamePrefix(String prefix) {
        this.modelNamePrefix = prefix;
        return this;
    }

    /**
     * Gets the removes the operation id prefix.
     *
     * @return the removes the operation id prefix
     */
    public boolean getRemoveOperationIdPrefix() {
        return removeOperationIdPrefix;
    }

    /**
     * Sets the remove operation id prefix.
     *
     * @param removeOperationIdPrefix the remove operation id prefix
     * @return the codegen configurator
     */
    public CodegenConfigurator setRemoveOperationIdPrefix(boolean removeOperationIdPrefix) {
        this.removeOperationIdPrefix = removeOperationIdPrefix;
        return this;
    }

    /**
     * Gets the model name suffix.
     *
     * @return the model name suffix
     */
    public String getModelNameSuffix() {
        return modelNameSuffix;
    }

    /**
     * Sets the model name suffix.
     *
     * @param suffix the suffix
     * @return the codegen configurator
     */
    public CodegenConfigurator setModelNameSuffix(String suffix) {
        this.modelNameSuffix = suffix;
        return this;
    }

    /**
     * Checks if is verbose.
     *
     * @return true, if is verbose
     */
    public boolean isVerbose() {
        return verbose;
    }

    /**
     * Sets the verbose.
     *
     * @param verbose the verbose
     * @return the codegen configurator
     */
    public CodegenConfigurator setVerbose(boolean verbose) {
        this.verbose = verbose;
        return this;
    }

    /**
     * Checks if is skip overwrite.
     *
     * @return true, if is skip overwrite
     */
    public boolean isSkipOverwrite() {
        return skipOverwrite;
    }

    /**
     * Sets the skip overwrite.
     *
     * @param skipOverwrite the skip overwrite
     * @return the codegen configurator
     */
    public CodegenConfigurator setSkipOverwrite(boolean skipOverwrite) {
        this.skipOverwrite = skipOverwrite;
        return this;
    }

    /**
     * Gets the lang.
     *
     * @return the lang
     */
    public String getLang() {
        return lang;
    }

    /**
     * Gets the template dir.
     *
     * @return the template dir
     */
    public String getTemplateDir() {
        return templateDir;
    }

    /**
     * Sets the template dir.
     *
     * @param templateDir the template dir
     * @return the codegen configurator
     */
    public CodegenConfigurator setTemplateDir(String templateDir) {
        File f = new File(templateDir);

        // check to see if the folder exists
        if (!(f.exists() && f.isDirectory())) {
            throw new IllegalArgumentException("Template directory " + templateDir + " does not exist.");
        }

        this.templateDir = f.getAbsolutePath();
        return this;
    }

    /**
     * Gets the auth.
     *
     * @return the auth
     */
    public String getAuth() {
        return auth;
    }

    /**
     * Sets the auth.
     *
     * @param auth the auth
     * @return the codegen configurator
     */
    public CodegenConfigurator setAuth(String auth) {
        this.auth = auth;
        return this;
    }

    /**
     * Gets the api package.
     *
     * @return the api package
     */
    public String getApiPackage() {
        return apiPackage;
    }

    /**
     * Sets the api package.
     *
     * @param apiPackage the api package
     * @return the codegen configurator
     */
    public CodegenConfigurator setApiPackage(String apiPackage) {
        this.apiPackage = apiPackage;
        return this;
    }

    /**
     * Gets the invoker package.
     *
     * @return the invoker package
     */
    public String getInvokerPackage() {
        return invokerPackage;
    }

    /**
     * Sets the invoker package.
     *
     * @param invokerPackage the invoker package
     * @return the codegen configurator
     */
    public CodegenConfigurator setInvokerPackage(String invokerPackage) {
        this.invokerPackage = invokerPackage;
        return this;
    }

    /**
     * Gets the group id.
     *
     * @return the group id
     */
    public String getGroupId() {
        return groupId;
    }

    /**
     * Sets the group id.
     *
     * @param groupId the group id
     * @return the codegen configurator
     */
    public CodegenConfigurator setGroupId(String groupId) {
        this.groupId = groupId;
        return this;
    }

    /**
     * Gets the artifact id.
     *
     * @return the artifact id
     */
    public String getArtifactId() {
        return artifactId;
    }

    /**
     * Sets the artifact id.
     *
     * @param artifactId the artifact id
     * @return the codegen configurator
     */
    public CodegenConfigurator setArtifactId(String artifactId) {
        this.artifactId = artifactId;
        return this;
    }

    /**
     * Gets the artifact version.
     *
     * @return the artifact version
     */
    public String getArtifactVersion() {
        return artifactVersion;
    }

    /**
     * Sets the artifact version.
     *
     * @param artifactVersion the artifact version
     * @return the codegen configurator
     */
    public CodegenConfigurator setArtifactVersion(String artifactVersion) {
        this.artifactVersion = artifactVersion;
        return this;
    }

    /**
     * Gets the system properties.
     *
     * @return the system properties
     */
    public Map<String, String> getSystemProperties() {
        return systemProperties;
    }

    /**
     * Sets the system properties.
     *
     * @param systemProperties the system properties
     * @return the codegen configurator
     */
    public CodegenConfigurator setSystemProperties(Map<String, String> systemProperties) {
        this.systemProperties = systemProperties;
        return this;
    }

    /**
     * Adds the system property.
     *
     * @param key the key
     * @param value the value
     * @return the codegen configurator
     */
    public CodegenConfigurator addSystemProperty(String key, String value) {
        this.systemProperties.put(key, value);
        return this;
    }

    /**
     * Gets the instantiation types.
     *
     * @return the instantiation types
     */
    public Map<String, String> getInstantiationTypes() {
        return instantiationTypes;
    }

    /**
     * Sets the instantiation types.
     *
     * @param instantiationTypes the instantiation types
     * @return the codegen configurator
     */
    public CodegenConfigurator setInstantiationTypes(Map<String, String> instantiationTypes) {
        this.instantiationTypes = instantiationTypes;
        return this;
    }

    /**
     * Adds the instantiation type.
     *
     * @param key the key
     * @param value the value
     * @return the codegen configurator
     */
    public CodegenConfigurator addInstantiationType(String key, String value) {
        this.instantiationTypes.put(key, value);
        return this;
    }

    /**
     * Gets the type mappings.
     *
     * @return the type mappings
     */
    public Map<String, String> getTypeMappings() {
        return typeMappings;
    }

    /**
     * Sets the type mappings.
     *
     * @param typeMappings the type mappings
     * @return the codegen configurator
     */
    public CodegenConfigurator setTypeMappings(Map<String, String> typeMappings) {
        this.typeMappings = typeMappings;
        return this;
    }

    /**
     * Adds the type mapping.
     *
     * @param key the key
     * @param value the value
     * @return the codegen configurator
     */
    public CodegenConfigurator addTypeMapping(String key, String value) {
        this.typeMappings.put(key, value);
        return this;
    }

    /**
     * Gets the additional properties.
     *
     * @return the additional properties
     */
    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    /**
     * Sets the additional properties.
     *
     * @param additionalProperties the additional properties
     * @return the codegen configurator
     */
    public CodegenConfigurator setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
        return this;
    }

    /**
     * Adds the additional property.
     *
     * @param key the key
     * @param value the value
     * @return the codegen configurator
     */
    public CodegenConfigurator addAdditionalProperty(String key, Object value) {
        this.additionalProperties.put(key, value);
        return this;
    }

    /**
     * Gets the import mappings.
     *
     * @return the import mappings
     */
    public Map<String, String> getImportMappings() {
        return importMappings;
    }

    /**
     * Sets the import mappings.
     *
     * @param importMappings the import mappings
     * @return the codegen configurator
     */
    public CodegenConfigurator setImportMappings(Map<String, String> importMappings) {
        this.importMappings = importMappings;
        return this;
    }

    /**
     * Adds the import mapping.
     *
     * @param key the key
     * @param value the value
     * @return the codegen configurator
     */
    public CodegenConfigurator addImportMapping(String key, String value) {
        this.importMappings.put(key, value);
        return this;
    }

    /**
     * Gets the language specific primitives.
     *
     * @return the language specific primitives
     */
    public Set<String> getLanguageSpecificPrimitives() {
        return languageSpecificPrimitives;
    }

    /**
     * Sets the language specific primitives.
     *
     * @param languageSpecificPrimitives the language specific primitives
     * @return the codegen configurator
     */
    public CodegenConfigurator setLanguageSpecificPrimitives(Set<String> languageSpecificPrimitives) {
        this.languageSpecificPrimitives = languageSpecificPrimitives;
        return this;
    }

    /**
     * Adds the language specific primitive.
     *
     * @param value the value
     * @return the codegen configurator
     */
    public CodegenConfigurator addLanguageSpecificPrimitive(String value) {
        this.languageSpecificPrimitives.add(value);
        return this;
    }

    /**
     * Gets the library.
     *
     * @return the library
     */
    public String getLibrary() {
        return library;
    }

    /**
     * Sets the library.
     *
     * @param library the library
     * @return the codegen configurator
     */
    public CodegenConfigurator setLibrary(String library) {
        this.library = library;
        return this;
    }

    /**
     * Gets the git user id.
     *
     * @return the git user id
     */
    public String getGitUserId() {
        return gitUserId;
    }

    /**
     * Sets the git user id.
     *
     * @param gitUserId the git user id
     * @return the codegen configurator
     */
    public CodegenConfigurator setGitUserId(String gitUserId) {
        this.gitUserId = gitUserId;
        return this;
    }

    /**
     * Gets the git repo id.
     *
     * @return the git repo id
     */
    public String getGitRepoId() {
        return gitRepoId;
    }

    /**
     * Sets the git repo id.
     *
     * @param gitRepoId the git repo id
     * @return the codegen configurator
     */
    public CodegenConfigurator setGitRepoId(String gitRepoId) {
        this.gitRepoId = gitRepoId;
        return this;
    }

    /**
     * Gets the release note.
     *
     * @return the release note
     */
    public String getReleaseNote() {
        return releaseNote;
    }

    /**
     * Sets the release note.
     *
     * @param releaseNote the release note
     * @return the codegen configurator
     */
    public CodegenConfigurator setReleaseNote(String releaseNote) {
        this.releaseNote = releaseNote;
        return this;
    }

    /**
     * Gets the http user agent.
     *
     * @return the http user agent
     */
    public String getHttpUserAgent() {
        return httpUserAgent;
    }

    /**
     * Sets the http user agent.
     *
     * @param httpUserAgent the http user agent
     * @return the codegen configurator
     */
    public CodegenConfigurator setHttpUserAgent(String httpUserAgent) {
        this.httpUserAgent= httpUserAgent;
        return this;
    }

    /**
     * Gets the reserved words mappings.
     *
     * @return the reserved words mappings
     */
    public  Map<String, String> getReservedWordsMappings() {
        return reservedWordMappings;
    }

    /**
     * Sets the reserved words mappings.
     *
     * @param reservedWordsMappings the reserved words mappings
     * @return the codegen configurator
     */
    public CodegenConfigurator setReservedWordsMappings(Map<String, String> reservedWordsMappings) {
        this.reservedWordMappings = reservedWordsMappings;
        return this;
    }

    /**
     * Adds the additional reserved word mapping.
     *
     * @param key the key
     * @param value the value
     * @return the codegen configurator
     */
    public CodegenConfigurator addAdditionalReservedWordMapping(String key, String value) {
        this.reservedWordMappings.put(key, value);
        return this;
    }

    /**
     * Gets the ignore file override.
     *
     * @return the ignore file override
     */
    public String getIgnoreFileOverride() {
        return ignoreFileOverride;
    }

    /**
     * Sets the ignore file override.
     *
     * @param ignoreFileOverride the ignore file override
     * @return the codegen configurator
     */
    public CodegenConfigurator setIgnoreFileOverride(final String ignoreFileOverride) {
        this.ignoreFileOverride = ignoreFileOverride;
        return this;
    }

    /**
     * To client opt input.
     *
     * @return the client opt input
     */
    public ClientOptInput toClientOptInput() {

        Validate.notEmpty(lang, "language must be specified");
        Validate.notEmpty(inputSpec, "input spec must be specified");

        setVerboseFlags();
        setSystemProperties();

        CodegenConfig config;
        
		try {
			config = (CodegenConfig) JavaBianCodegen.class.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException("Can't load config class for bian codegen", e);
		}

        config.setInputSpec(inputSpec);
        config.setOutputDir(outputDir);
        config.setSkipOverwrite(skipOverwrite);
        config.setIgnoreFilePathOverride(ignoreFileOverride);
        config.setRemoveOperationIdPrefix(removeOperationIdPrefix);

        config.instantiationTypes().putAll(instantiationTypes);
        config.typeMapping().putAll(typeMappings);
        config.importMapping().putAll(importMappings);
        config.languageSpecificPrimitives().addAll(languageSpecificPrimitives);
        config.reservedWordsMappings().putAll(reservedWordMappings);

        checkAndSetAdditionalProperty(apiPackage, CodegenConstants.API_PACKAGE);
        checkAndSetAdditionalProperty(modelPackage, CodegenConstants.MODEL_PACKAGE);
        checkAndSetAdditionalProperty(invokerPackage, CodegenConstants.INVOKER_PACKAGE);
        checkAndSetAdditionalProperty(groupId, CodegenConstants.GROUP_ID);
        checkAndSetAdditionalProperty(artifactId, CodegenConstants.ARTIFACT_ID);
        checkAndSetAdditionalProperty(artifactVersion, CodegenConstants.ARTIFACT_VERSION);
        checkAndSetAdditionalProperty(templateDir, toAbsolutePathStr(templateDir), CodegenConstants.TEMPLATE_DIR);
        checkAndSetAdditionalProperty(modelNamePrefix, CodegenConstants.MODEL_NAME_PREFIX);
        checkAndSetAdditionalProperty(modelNameSuffix, CodegenConstants.MODEL_NAME_SUFFIX);
        checkAndSetAdditionalProperty(gitUserId, CodegenConstants.GIT_USER_ID);
        checkAndSetAdditionalProperty(gitRepoId, CodegenConstants.GIT_REPO_ID);
        checkAndSetAdditionalProperty(releaseNote, CodegenConstants.RELEASE_NOTE);
        checkAndSetAdditionalProperty(httpUserAgent, CodegenConstants.HTTP_USER_AGENT);

        handleDynamicProperties(config);

        if (isNotEmpty(library)) {
            config.setLibrary(library);
        }

        config.additionalProperties().putAll(additionalProperties);

        ClientOptInput input = new ClientOptInput()
                .config(config);

        final List<AuthorizationValue> authorizationValues = AuthParser.parse(auth);

        Swagger swagger = new SwaggerParser().read(inputSpec, authorizationValues, true);

        input.opts(new ClientOpts())
                .swagger(swagger);

        return input;
    }

    /**
     * Adds the dynamic property.
     *
     * @param name the name
     * @param value the value
     * @return the codegen configurator
     */
    @JsonAnySetter
    public CodegenConfigurator addDynamicProperty(String name, Object value) {
        dynamicProperties.put(name, value);
        return this;
    }

    /**
     * Gets the dynamic properties.
     *
     * @return the dynamic properties
     */
    @JsonAnyGetter
    public Map<String, Object> getDynamicProperties() {
        return dynamicProperties;
    }

    /**
     * Handle dynamic properties.
     *
     * @param codegenConfig the codegen config
     */
    private void handleDynamicProperties(CodegenConfig codegenConfig) {
        for (CliOption langCliOption : codegenConfig.cliOptions()) {
            String opt = langCliOption.getOpt();
            if (dynamicProperties.containsKey(opt)) {
                codegenConfig.additionalProperties().put(opt, dynamicProperties.get(opt));
            }
            else if(systemProperties.containsKey(opt)) {
                codegenConfig.additionalProperties().put(opt, systemProperties.get(opt));
            }
        }
    }

    /**
     * Sets the verbose flags.
     */
    private void setVerboseFlags() {
        if (!verbose) {
            return;
        }
        LOGGER.info("\nVERBOSE MODE: ON. Additional debug options are injected" +
                "\n - [debugSwagger] prints the swagger specification as interpreted by the codegen" +
                "\n - [debugModels] prints models passed to the template engine" +
                "\n - [debugOperations] prints operations passed to the template engine" +
                "\n - [debugSupportingFiles] prints additional data passed to the template engine");

        System.setProperty("debugSwagger", "");
        System.setProperty("debugModels", "");
        System.setProperty("debugOperations", "");
        System.setProperty("debugSupportingFiles", "");
    }

    /**
     * Sets the system properties.
     */
    private void setSystemProperties() {
        for (Map.Entry<String, String> entry : systemProperties.entrySet()) {
            System.setProperty(entry.getKey(), entry.getValue());
        }
    }

    /**
     * To absolute path str.
     *
     * @param path the path
     * @return the string
     */
    private static String toAbsolutePathStr(String path) {
        if (isNotEmpty(path)) {
            return Paths.get(path).toAbsolutePath().toString();
        }

        return path;

    }

    /**
     * Check and set additional property.
     *
     * @param property the property
     * @param propertyKey the property key
     */
    private void checkAndSetAdditionalProperty(String property, String propertyKey) {
        checkAndSetAdditionalProperty(property, property, propertyKey);
    }

    /**
     * Check and set additional property.
     *
     * @param property the property
     * @param valueToSet the value to set
     * @param propertyKey the property key
     */
    private void checkAndSetAdditionalProperty(String property, String valueToSet, String propertyKey) {
        if (isNotEmpty(property)) {
            additionalProperties.put(propertyKey, valueToSet);
        }
    }

    /**
     * From file.
     *
     * @param configFile the config file
     * @return the codegen configurator
     */
    public static CodegenConfigurator fromFile(String configFile) {

        if (isNotEmpty(configFile)) {
            try {
                return Json.mapper().readValue(new File(configFile), CodegenConfigurator.class);
            } catch (IOException e) {
                LOGGER.error("Unable to deserialize config file: " + configFile, e);
            }
        }
        return null;
    }

}
