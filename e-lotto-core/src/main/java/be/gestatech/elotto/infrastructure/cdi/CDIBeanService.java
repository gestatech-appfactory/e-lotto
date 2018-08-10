package be.gestatech.elotto.infrastructure.cdi;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.*;
import javax.inject.Named;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Objects;
import java.util.logging.Logger;

@Named
public class CDIBeanService {

    private static final Logger LOGGER = Logger.getLogger(CDIBeanService.class.getName());

    private static CDIBeanService INSTANCE = new CDIBeanService();

    private CDIBeanService() {
        super();
    }

    public static CDIBeanService getInstance() {
        return INSTANCE;
    }

    public <T> T getCDIBean(final Class<T> clazz) {
        BeanManager manager = getBeanManager();
        if (Objects.nonNull(manager)) {
            Bean<T> bean = (Bean<T>) manager.getBeans(clazz).iterator().next();
            CreationalContext<T> ctx = manager.createCreationalContext(bean);
            return (T) manager.getReference(bean, clazz, ctx);
        } else {
            try {
                return clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                LOGGER.warning("Could not create instance of class <" + clazz + ">.");
            }
            return null;
        }
    }

    public <T> T getCDIBeanByName(final String name, Class<T> clazz) {
        BeanManager bm = getBeanManager();
        Bean<T> bean = (Bean<T>) bm.getBeans(name).iterator().next();
        CreationalContext<T> ctx = bm.createCreationalContext(bean);
        return (T) bm.getReference(bean, clazz, ctx);
    }

    private BeanManager getBeanManager() {
        CDI<Object> containerAccessor;
        try {
            containerAccessor = CDI.current();
            if (Objects.nonNull(containerAccessor)) {
                return containerAccessor.getBeanManager();
            } else {
                try {
                    InitialContext initialContext = new InitialContext();
                    return (BeanManager) initialContext.lookup("java:comp/BeanManager");
                } catch (NamingException e) {
                    return null;
                }
            }
        } catch (IllegalStateException ignore) {
        }
        return null;
    }

    /**
     * Used for unmanaged objects
     */
    public void injectManual(Object obj) {
        BeanManager beanManager = getBeanManager();
        AnnotatedType type = (AnnotatedType) beanManager.createAnnotatedType(obj.getClass());
        InjectionTarget it = beanManager.createInjectionTarget(type);
        CreationalContext instanceContext = beanManager.createCreationalContext(null);
        it.inject(obj, instanceContext); // calls the initializer methods and performs field injection
        it.postConstruct(obj); // finally call the @PostConstruct-annotated method
    }
}
