package org.example.domain;

import io.ebean.Ebean;
import io.ebean.EbeanServer;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * When running tests in the IDE install the "Enhancement plugin".
 * <p>
 * http://ebean-orm.github.io/docs/setup/enhancement#ide
 */
public class CustomerTest {

  Customer rob;
  /**
   * Get the "default server" and save().
   */
  @Test
  public void insert_via_server() {

    rob = new Customer("Rob");

    EbeanServer server = Ebean.getDefaultServer();
    server.save(rob);

    assertNotNull(rob.getId());
  }

  /**
   * Use the Ebean singleton (effectively using the "default server").
   */
  @Test(dependsOnMethods = "insert_via_server")
  public void insert_via_ebean() {

    Customer jim = new Customer("Jim");
    Ebean.save(jim);

    assertNotNull(jim.getId());
  }


  /**
   * Find and then update.
   */
  @Test(dependsOnMethods = "insert_via_server")
  public void updateRob() {

    Customer rob = Ebean.find(Customer.class)
        .where().eq("name", "Rob")
        .findUnique();

    rob.setNotes("Doing an update");
    Ebean.save(rob);
  }

  /**
   * Execute an update without a prior query.
   */
  @Test(dependsOnMethods = "updateRob")
  public void statelessUpdate() {

    Customer upd = new Customer();
    upd.setId(rob.getId());
    upd.setNotes("Update without a fetch");

    Ebean.update(upd);
  }

  @Test
  public void cascadeOneToMany() {
    Customer jim = new Customer("Joe");
    jim.getOrders().add(new Order(5L));
    Ebean.save(jim);

    assertNotNull(jim.getId());
  }

  @Test(dependsOnMethods = "cascadeOneToMany")
  public void refreshAndUpdateOneToMany() {
    Customer jim = Ebean.createQuery(Customer.class).where().eq("name", "Joe").findUnique();
    assertEquals(jim.getOrders().size(), 1);

    jim.getOrders().add(new Order(6L));
    Ebean.save(jim);
  }

  @Test(dependsOnMethods = "refreshAndUpdateOneToMany")
  public void refresUpdatedOneToMany() {
    Customer jim = Ebean.createQuery(Customer.class).where().eq("name", "Joe").findUnique();
    assertEquals(jim.getOrders().size(), 2);
  }

}