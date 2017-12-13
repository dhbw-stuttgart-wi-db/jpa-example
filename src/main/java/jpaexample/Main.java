package jpaexample;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jpaexample.dto.Kurs;
import jpaexample.dto.Student;

public class Main {
	static final Logger log;
	static final String PERSISTENCE_UNIT_NAME = "dhbw";

	static {
		configureLog4JFromClasspath();
		log = LoggerFactory.getLogger(Main.class);
	}

	public static void main(String[] args) throws IOException {
		log.debug("Startup");

		EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
		EntityManager em = factory.createEntityManager();

		createSomeStudents(em);
		printAllStudents(em);
		deleteAllStudents(em);

		// Good practice: close EntityManager to release resources
		em.close();

		log.debug("Shutdown");
		System.exit(0);
	}

	private static void createSomeStudents(EntityManager em) {
		// Begin a new local transaction so that we can persist a new entity
		em.getTransaction().begin();

		Kurs kurs = new Kurs();
		kurs.setDescription("Family for the Knopfs");
		em.persist(kurs);
		for (int i = 0; i < 30; i++) {
			Student person = new Student();
			person.setFirstName("Jim_" + i);
			person.setLastName("Knopf_" + i);
			em.persist(person);

			// now persists the family person relationship
			kurs.getMembers().add(person);
			em.persist(person);
			em.persist(kurs);
		}
		em.getTransaction().commit();
	}

	private static void printAllStudents(EntityManager em) {
		em.getTransaction().begin();
		Query q = em.createQuery("select s from Student s");

		@SuppressWarnings("unchecked")
		List<Student> list = q.getResultList();

		for (Student student : list) {
			log.debug("Student: {}", student.getLastName());
		}

		em.getTransaction().commit();
	}

	private static void deleteAllStudents(EntityManager em) {
		em.getTransaction().begin();
		int changedElements = em.createQuery("delete from Student s").executeUpdate();
		log.debug("Deleted {} entities.", changedElements);
		em.getTransaction().commit();
	}

	public static void configureLog4JFromClasspath() {
		InputStream log4JPropertiesStream = Main.class.getClassLoader().getResourceAsStream("log4j.properties");

		if (log4JPropertiesStream != null) {
			try {
				Properties properties = new Properties();
				properties.load(log4JPropertiesStream);
				PropertyConfigurator.configure(properties);
			} catch (Exception e) {
				System.err.println("Tried to load log4j configuration from classpath, resulting in the following exception: {}" + e);
				System.err.println("Using default logging configuration.");
			}
		}
	}

}
