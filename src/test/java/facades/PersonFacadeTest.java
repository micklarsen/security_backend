package facades;

import dto.PersonDTO;
import dto.PersonsDTO;
import entities.Role;
import utils.EMF_Creator;
import entities.Person;
import errorhandling.NotFoundException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import static org.hamcrest.MatcherAssert.assertThat;
import org.hamcrest.Matchers;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import security.errorhandling.AuthenticationException;

//Uncomment the line below, to temporarily disable this test
//@Disabled
public class PersonFacadeTest {

    private static EntityManagerFactory emf;
    //private static FacadeExample facade;
    private static PersonFacade facade;
    private Person p1, p2, p3;
    private Role r1, r2;

    public PersonFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = PersonFacade.getPersonFacade(emf);
    }

    @AfterAll
    public static void tearDownClass() {
//        Clean up database after test is done or use a persistence unit with drop-and-create to start up clean on every test
    }

    // Setup the DataBase in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the code below to use YOUR OWN entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        try {
//Delete existing users and roles to get a "fresh" database
            em.getTransaction().begin();
            em.createQuery("delete from Person").executeUpdate();
            em.createQuery("delete from Role").executeUpdate();

            p1 = new Person("someone@hotmail.com", "someone", "secretpassword", "13467964", "John", "Williams");
            p2 = new Person("villads@gmail.com", "vill4ds", "secretpassword", "65478931", "Villads", "Markmus");
            p3 = new Person("someoneelse@hotmail.com", "someoneElse", "secretpassword", "32132112", "Willy", "Keeper");

            r1 = new Role("user");
            r2 = new Role("admin");
            p1.addRole(r1);
            p2.addRole(r2);
            p3.addRole(r1);
            p3.addRole(r2);
            em.persist(r1);
            em.persist(r2);
            em.persist(p1);
            em.persist(p2);
            em.persist(p3);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @AfterEach
    public void tearDown() {
//        Remove any data after each test was run
    }

    @Test
    public void testPersonCount() {
        assertEquals(3, facade.getUserCount(), "Expects three rows in the database");
    }

    @Test
    public void testGetVeryfiedUser() throws AuthenticationException {
        String pass = p1.getUserPass();

        assertEquals(p1.getEmail(), "someone@hotmail.com");
        assertEquals(p1.getUserPass(), pass);
        assertThat(p1.getEmail(), is(not("pollemand")));
        assertThat(p1.getUserPass(), is(not("lilleGrimTomat")));
    }

    @Test
    public void testGetRoleList() {
        assertEquals(p1.getRolesAsStrings().get(0), r1.getRoleName());
    }

    @Test
    public void testMakePerson() throws Exception {
        EntityManager em = emf.createEntityManager();

        String email = "ulla@hotmail.com";
        String username = "ulla";
        String phone = "123456";
        String firstName = "Ulla";
        String lastName = "Allu";
        String password = "muffe";

        Person p = new Person(email, username, password, phone, firstName, lastName);


        System.out.println("Person: " + p);

        PersonDTO pDTO = new PersonDTO(p);

        System.out.println("PersonDTO" + pDTO);

        facade.makePerson(pDTO);

        try {
            em.getTransaction().begin();

            em.find(Person.class, email);

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Test
    public void testGetAllPerson() throws NotFoundException {

        PersonsDTO personsDTO = facade.getAllPersons();
        List<PersonDTO> list = personsDTO.getAll();
        System.out.println("Liste af personer: " + list);
        assertThat(list, everyItem(Matchers.hasProperty("email")));
        assertThat(list, Matchers.hasItems(Matchers.<PersonDTO>hasProperty("firstName", is("John")),
                Matchers.<PersonDTO>hasProperty("firstName", is("Villads"))
        ));

    }

    @Test
    public void getPersonByEmail() throws NotFoundException {

        PersonDTO personDTO = facade.getPersonByEmail(p1.getEmail());

        assertEquals("someone@hotmail.com", personDTO.getEmail());
    }

    @Test
    public void testDeletePerson() throws NotFoundException {

        PersonDTO personDTO = facade.deletePerson(p1.getEmail());
        //assertThat(p1.getEmail(), is(not(personDTO.getEmail())));
        assertEquals(2, facade.getUserCount());

    }

    @Test
    public void TestUpdatePerson() throws NotFoundException {

        EntityManager em = emf.createEntityManager();

        String phone = "123456";
        String firstName = "Ulla";
        String lastName = "Allu";

        p1.setFirstName(firstName);
        p1.setLastName(lastName);
        p1.setPhone(phone);
        System.out.println("Person: " + p1);

        PersonDTO pDTO = new PersonDTO(p1);

        System.out.println("PersonDTO: " + pDTO);

        facade.updatePerson(pDTO);

        try {
            em.getTransaction().begin();
            em.merge(p1);
            em.getTransaction().commit();
        } finally {
            em.close();
        }

        assertEquals(p1.getFirstName(), "Ulla");
    }

}
