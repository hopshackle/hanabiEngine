package com.fossgalaxy.games.fireworks.utils.agentbuilder;

import com.fossgalaxy.games.fireworks.ai.Agent;
import com.fossgalaxy.games.fireworks.ai.mcts.MCTS;
import com.fossgalaxy.games.fireworks.ai.vanDenBergh.VanDenBergh;
import com.fossgalaxy.games.fireworks.annotations.AgentConstructor;
import com.fossgalaxy.games.fireworks.annotations.Parameter;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by webpigeon on 06/04/17.
 */
public class AgentFinder {
    private static final Logger logger = LoggerFactory.getLogger(AgentFinder.class);
    private final Map<Class<?>, Function<String, ?>> converters;
    private final Map<String, AgentFactory> knownFactories;

    //have we already scanned for agents?
    private boolean hasScanned;

    public AgentFinder() {
        this.converters = new HashMap<>();
        this.knownFactories = new HashMap<>();
        this.hasScanned = false;

        buildConverters();
    }

    /**
     * A default list of converters that we understand.
     */
    private void buildConverters() {
        converters.put(String.class, Function.identity());
        converters.put(Integer.class, Integer::parseInt);
        converters.put(int.class, Integer::parseInt);
        converters.put(Double.class, Double::parseDouble);
        converters.put(double.class, Double::parseDouble);
        converters.put(Float.class, Float::parseFloat);
        converters.put(float.class, Float::parseFloat);
        converters.put(Boolean.class, Boolean::parseBoolean);
        converters.put(boolean.class, Boolean::parseBoolean);
    }

    /**
     * Allow creation of custom converters.
     *
     * @param clazz     the class to convert
     * @param converter the converter to use
     * @param <T>       the type that we expect to convert to.
     */
    public <T> void addConverter(Class<T> clazz, Function<String, T> converter) {
        converters.put(clazz, converter);
    }

    /**
     * Allow manual insertion of factories.
     *
     * @param name    the name of the factory
     * @param factory the method that creates the agent
     */
    public void addFactory(String name, AgentFactory factory) {
        knownFactories.put(name, factory);
    }

    /**
     * Generate an agent with a given name from this factory
     *
     * @param name the name of the agent
     * @param args the arguments to pass to the factory
     * @return The constructed agent
     */
    public Agent buildAgent(String name, String... args) {
        if (!hasScanned) {
            scanForAgents();
        }

        AgentFactory factory = knownFactories.get(name);
        if (factory == null) {
            throw new IllegalArgumentException("Unknown factory type");
        }

        return factory.build(args);
    }

    public static void main(String[] args) {
        AgentFinder finder = new AgentFinder();

        //as a test build our modified MCTS agent
        MCTS rmhc = (MCTS) finder.buildAgent("mcts", "50", "6", "10");
        System.out.println(rmhc);
        VanDenBergh van = (VanDenBergh) finder.buildAgent("vandenbergh", "0.6", "1.0", "NEXT_USEFUL_THEN_MOST_CARDS", "MOST_CERTAIN_IS_USELESS");
        System.out.println(van);
    }

    /**
     * A lazy-loaded class scanner.
     * <p>
     * This scans the whole classpath for classes that extend agent, and builds factories for them.
     */
    private void scanForAgents() {

        //ensure that we do not scan more than once - once is enough.
        if (hasScanned) {
            return;
        }

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forJavaClassPath())
        .setExpandSuperTypes(false));

        //find all subtypes of the agent class
        Set<Class<? extends Agent>> agentClazzes = reflections.getSubTypesOf(Agent.class);
        for (Class<? extends Agent> agentClazz : agentClazzes) {

            //skip the class if it is abstract or not public.
            int classMods = agentClazz.getModifiers();
            if (Modifier.isAbstract(classMods) || !Modifier.isPublic(classMods)) {
                continue;
            }

            try {
                AgentFactory factory = buildFactory(agentClazz);
                knownFactories.put(factory.name(), factory);
            } catch (IllegalArgumentException ex) {
                logger.error("Failed to create agent " + agentClazz, ex);
            }
        }

        //ensure we do not scan again
        hasScanned = true;
    }

    public AgentFactory buildFactory(Class<? extends Agent> agentClazz) {

        Constructor<?> bestMatch = null;

        Constructor<?>[] constructors = agentClazz.getConstructors();
        for (Constructor<?> constructor : constructors) {
            if (constructor.getParameterCount() == 0 && Modifier.isPublic(constructor.getModifiers())) {
                bestMatch = constructor;
            } else {
                AgentConstructor builder = constructor.getAnnotation(AgentConstructor.class);
                if (builder == null) {
                    continue;
                }

                String name = builder.value().equals("") ? agentClazz.getSimpleName() : builder.value();
                bestMatch = constructor;

                Class<?>[] params = constructor.getParameterTypes();
                Function<String, ?>[] convertersInst = (Function[]) Array.newInstance(Function.class, params.length);

                HashMap<Integer, Parameter> parameters = new HashMap<>();
                for(Parameter p : constructor.getAnnotationsByType(Parameter.class)){
                    if(!parameters.containsKey(p.id())){
                        parameters.put(p.id(), p);
                    }
                }

                for (int i = 0; i < params.length; i++) {
                    System.out.println(agentClazz + " " + i + " -> " + params[i]);
                    if(parameters.containsKey(i)){
                        Parameter parameter = parameters.get(i);
                        try {
                            Method methodWithThatName = agentClazz.getMethod(parameter.func(), String.class);
                            if (Modifier.isPublic(methodWithThatName.getModifiers()) && Modifier.isStatic(methodWithThatName.getModifiers())) {

                                if (!methodWithThatName.getReturnType().isAssignableFrom(params[i])) {
                                    throw new IllegalArgumentException("you said params " + i + " was a " + params[i] + " but the converter wants to give me a " + methodWithThatName.getReturnType());
                                }
                                convertersInst[i] = (s) -> getConverter(methodWithThatName, s);
                            }
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        }
                    }else{
                        // Try to handle enums with a default
                        if(params[i].isEnum()){
                            final Class enumClass = params[i];
                            convertersInst[i] = (s) -> Enum.valueOf(enumClass, s);
                        }else {
                            convertersInst[i] = converters.get(params[i]);
                        }
                    }
                }
                return new ConstructorFactory(agentClazz, constructor, convertersInst, name);
            }

        }

        if (bestMatch == null) {
            throw new IllegalArgumentException("You must either annotate a constructor or provide a public no-args constructor");
        }

        return new ConstructorFactory(agentClazz, bestMatch, null);
    }

    private Object getConverter(Method methodWithThatName, String s) {
        try {
            return methodWithThatName.invoke(null, s);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Map<String, AgentFactory> getFactories() {
        return Collections.unmodifiableMap(knownFactories);
    }
}
