package GAI.hibernate;

import javax.persistence.*;
import java.lang.*;

public class AppDriver {
    public static void main(String[] args){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("GAI_persistence");
        EntityManager em = emf.createEntityManager();

        System.out.println("Test started");
        em.getTransaction().begin();
        Driver st = new Driver();
        st.setName("Test Group");
        em.persist(st);
        em.getTransaction().commit();

        System.out.println("New station ID is " + st.getIdDriver());
    }

}