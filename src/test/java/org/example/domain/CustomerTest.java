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
  public void cascadeOneToMany_FAIL() {
    Customer joe = new Customer("JoeFails");
    joe.getOrders().add(new Order(5L)); // this isn't inserted, 'cause the Order entity has no other property set than the @ID.
    Ebean.save(joe);

    assertNotNull(joe.getId());
  }

  @Test(dependsOnMethods = "cascadeOneToMany_FAIL")
  public void refreshOneToMany_FAIL() {
    Customer joe = Ebean.createQuery(Customer.class).where().eq("name", "JoeFails").findUnique();
    assertEquals(joe.getOrders().size(), 1); // fails, beacuse the Order wasn't cascaded in the previous method
  }

  @Test
  public void cascadeOneToMany() {
    Customer joe = new Customer("Joe");
    Order order = new Order(5L, "someOtherPropertyThanIdIsNecessary");
    joe.getOrders().add(order);
    Ebean.save(joe);

    assertNotNull(joe.getId());
  }

  @Test(dependsOnMethods = "cascadeOneToMany")
  public void refreshAndUpdateOneToMany() {
    Customer joe = Ebean.createQuery(Customer.class).where().eq("name", "Joe").findUnique();
    assertEquals(joe.getOrders().size(), 1);

    joe.getOrders().add(new Order(6L, "someRandomProperty")); // this line translated to an update statement rather than an insert.
    Ebean.save(joe);
  }

  @Test(dependsOnMethods = "refreshAndUpdateOneToMany")
  public void refresUpdatedOneToMany() {
    Customer joe = Ebean.createQuery(Customer.class).where().eq("name", "Joe").findUnique();
    assertEquals(joe.getOrders().size(), 2); // this fails, 'cause the update in the previous method
  }

}