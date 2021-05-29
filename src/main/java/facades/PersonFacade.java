package facades;

import dto.PersonDTO;
import dto.PersonsDTO;
import entities.Person;
import entities.Role;
import errorhandling.NotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import security.errorhandling.AuthenticationException;

public class PersonFacade {

    private static EntityManagerFactory emf;
    private static PersonFacade instance;

    /**
     * Sanitizer helper for email using regex to match tainted source
     * Regex check for characters before and after '@' and a '.' and
     * minimum 2 and maximum 4 character af the '.'
     * @param matchText
     * @return
     */
    public boolean regexEmailCheck(String matchText){
        String regex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        return matchText.matches(regex);
    }

    /**
     * Sanitizer helper for character check using regex to match tainted source
     * Regex check for only letters, digits, _ and -
     * @param matchText
     * @return
     */
    public boolean regexCharacterCheck(String matchText){
        String regex = "^[a-zA-Z0-9_-]*$";
        return matchText.matches(regex);
    }

    /**
     * Sanitizer helper for password check using regex to match tainted source
     * Regex check for minimum one uppercase, one lowercase, one digit, one
     * special character and minimum 6 character
     * @param matchText
     * @return
     */
    public boolean regexPasswordCheck(String matchText){
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,}$";
        return matchText.matches(regex);
    }

    private PersonFacade() {
    }

    /**
     * @param _emf
     * @return the instance of this facade.
     */
    public static PersonFacade getPersonFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new PersonFacade();
        }
        return instance;
    }

    public long getUserCount() {
        EntityManager em = emf.createEntityManager();
        try {
            long userCount = (long) em.createQuery("SELECT COUNT(u) FROM Person u").getSingleResult();
            return userCount;
        } finally {
            em.close();
        }
    }

    public Person getVeryfiedUser(String username, String password) throws AuthenticationException {
        EntityManager em = emf.createEntityManager();
        Person user;
        try {
            user = em.find(Person.class, username);
            if (user == null || !user.verifyPassword(password)) {
                throw new AuthenticationException("Invalid user name or password");
            }
        } finally {
            em.close();
        }
        return user;
    }

    public PersonDTO getPersonByEmail(String email) throws NotFoundException {
        EntityManager em = emf.createEntityManager();

        Person user;
        PersonDTO pDTO;

        try {
            user = em.find(Person.class, email);
            if (user == null) {
                throw new NotFoundException("email");
            }
        } finally {
            em.close();
        }
        return new PersonDTO(user);
    }

    public PersonsDTO getAllPersons() throws NotFoundException {

        EntityManager em = emf.createEntityManager();
        PersonsDTO psDTO;
        try {
            psDTO = new PersonsDTO(em.createQuery("SELECT p FROM Person p").getResultList());
        } catch (Exception e) {
            throw new NotFoundException("No connection to the database");
        } finally {
            em.close();
        }
        return psDTO;

    }

    public PersonDTO makePerson(PersonDTO person) throws AuthenticationException {

        /**
         * Taint source:
         * Here we have the tainted source from our rest endpoint in PersonResource.java
         */
        String email = person.getEmail();
        String username = person.getUsername();
        String userPass = person.getPassword();
        String phone = person.getPhone();
        String fName = person.getFirstName();
        String lName = person.getLastName();

        /**
         * Sanitizers:
         * Here we sanitize email, username and password from the tainted source before we persist to db
         * We use helper methods with regex to check for bad input
         */
        if (!regexEmailCheck(email)){
            throw new AuthenticationException("Bad email format");
        }

        if(!regexCharacterCheck(username) || !(username.length() >= 2)){
            throw new AuthenticationException("Bad username - No special characters allowed and must be 2 characters or more");
        }

        if(!regexPasswordCheck(userPass)){
            throw  new AuthenticationException("Bad password - Minimum six characters, at least one uppercase letter, one lowercase letter, one number and one special character:");
        }

        Person newPerson;

        EntityManager em = emf.createEntityManager();

        try {
            newPerson = em.find(Person.class, email);

            long newUsername = (long) em.createQuery("SELECT COUNT(u) FROM Person u WHERE u.username = :username")
                    .setParameter("username", username).getSingleResult();

            if (newPerson == null && email.length() > 0 && userPass.length() > 0 && newUsername != 1) {
                newPerson = new Person(email, username, userPass, phone, fName, lName);
                Role userRole = em.find(Role.class, "user");
                newPerson.addRole(userRole);

                /**
                 * Sink:
                 * Here vi persist the sanitized source
                 */
                em.getTransaction().begin();
                em.persist(newPerson);
                em.getTransaction().commit();
            } else {

                /**
                 * Validation:
                 * Her we validate user input server side
                 * First check if there is a email or password
                 * Then if username exist and if email exist or both exist
                 */
                if ((email.length() == 0 || userPass.length() == 0)) {
                    throw new AuthenticationException("Missing input");
                }
                if (newUsername > 0 && newPerson == null) {
                    throw new AuthenticationException("Username exist");
                }
                if (newPerson.getEmail().equalsIgnoreCase(person.getEmail()) && !(newUsername > 0)) {
                    throw new AuthenticationException("User email exist");
                }

                if (newPerson.getUsername().equalsIgnoreCase(person.getUsername()) && (newPerson.getEmail().equalsIgnoreCase(person.getEmail()))) {
                    throw new AuthenticationException("Username and email exist");
                }
            }
        } finally {
            em.close();
        }
        return new PersonDTO(newPerson);
    }

    public void updatePerson(PersonDTO p) throws NotFoundException {
        EntityManager em = emf.createEntityManager();
        Person person = em.find(Person.class, p.getEmail());
        if (person == null) {
            throw new NotFoundException("No person found");
        }
        person.setFirstName(p.getFirstName());
        person.setLastName(p.getLastName());
        person.setPhone(p.getPhone());

        try {
            em.getTransaction().begin();
            em.merge(person);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public PersonDTO deletePerson(String email) throws NotFoundException {
        EntityManager em = emf.createEntityManager();
        Person person = em.find(Person.class, email);
        if (person == null) {
            throw new NotFoundException("Could not delete, provided id does not exist");
        } else {
            try {
                em.getTransaction().begin();
                em.remove(person);
                em.getTransaction().commit();
            } finally {
                em.close();
            }
            return new PersonDTO(person);
        }
    }

}
