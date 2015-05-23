package main.java;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Session;

public class WorkSql {
	public int addUser(User user) {
		Session session = InitHibernate.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			session.save(user);
		} catch (Exception e) {
			session.getTransaction().rollback();
			System.out.println("HELLO I AM A PROBLEM FROM USERS");
			return -1;
		}
		session.getTransaction().commit();
		return 0;
	}

	public int updateUser(User user) {
		Session session = InitHibernate.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			session.saveOrUpdate(user);
		} catch (Exception e) {
			session.getTransaction().rollback();
			return -1;
		}
		session.getTransaction().commit();
		return 0;
	}

	public User getUser(int idUser) {
		User user;
		Session session = InitHibernate.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			user = (User) session.load(User.class, idUser);
			Hibernate.initialize(user.getLogin());
		} catch (Exception e) {
			session.getTransaction().rollback();
			return null;
		}
		session.getTransaction().commit();
		return user;
	}

	public User getUserByLogin(String login) {
		User result = null;
		Session session = InitHibernate.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			@SuppressWarnings("unchecked")
			List<User> list = session.createQuery("from User order by idUser")
					.list();
			for (User a : list) {
				Hibernate.initialize(a.getLogin());
				if (a.getLogin().equals(login))
					result = a;
			}
		} catch (Exception e) {
			session.getTransaction().rollback();
			return null;
		}
		session.getTransaction().commit();

		return result;
	}

	@SuppressWarnings("unchecked")
	public List<User> findUser() {
		List<User> list;
		Session session = InitHibernate.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			list = session.createQuery("from User order by idUser").list();
			for (User user : list)
				Hibernate.initialize(user.getLogin());
		} catch (Exception e) {
			session.getTransaction().rollback();
			return null;
		}
		session.getTransaction().commit();
		return list;
	}

	@SuppressWarnings("unchecked")
	public int deleteUser(List<User> user) {
		Session session = InitHibernate.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			if (user.isEmpty())
				System.out.println("EMPTY");
			else {
				for (User a : user) {
					List<Include> result =  new LinkedList(Arrays.asList());
					List<Include> in = session.createQuery("from Include")
							.list();
					for (Include include : in) {
						Hibernate.initialize(include.getIdUser());
						if (include.getIdUser() == a.getIdUser()) {
							result.add(include);
						}
					}
					for (Include i : result){
						session.delete(i);
					}
					session.delete(a);
				}
			}
		} catch (Exception e) {
			session.getTransaction().rollback();
			return -1;
		}
		session.getTransaction().commit();
		return 0;
	}

	public int addEvent(Event event) {
		Session session = InitHibernate.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			session.save(event);
		} catch (Exception e) {
			session.getTransaction().rollback();
			System.out.println("HELLO I AM A PROBLEM IN EVENTS");
			return -1;
		}
		session.getTransaction().commit();
		return 0;
	}

	public int updateEvent(Event event) {
		Session session = InitHibernate.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			session.saveOrUpdate(event);
		} catch (Exception e) {
			session.getTransaction().rollback();
			return -1;
		}
		session.getTransaction().commit();
		return 0;
	}

	@SuppressWarnings("unchecked")
	public List<Event> findEvent() {
		List<Event> list;
		Session session = InitHibernate.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			list = session.createQuery("from Event order by idEvent").list();
			for (Event event : list)
				Hibernate.initialize(event.getNameEvent());
		} catch (Exception e) {
			session.getTransaction().rollback();
			return null;
		}
		session.getTransaction().commit();
		return list;
	}

	@SuppressWarnings("unchecked")
	public Event getEventByName(String name) {
		Event event = null;
		Session session = InitHibernate.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			List<Event> list = session.createQuery(
					"from Event order by idEvent").list();
			for (Event a : list) {
				Hibernate.initialize(a.getNameEvent());
				if (a.getNameEvent().equals(name)) {
					event = a;
				}
			}
		} catch (Exception e) {
			session.getTransaction().rollback();
			return null;
		}
		session.getTransaction().commit();
		return event;
	}

	public int deleteEvent(List<Event> list) {
		Session session = InitHibernate.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			for (Event a : list) {
				List<Include> result = new LinkedList(Arrays.asList());
				@SuppressWarnings("unchecked")
				List<Include> in = session.createQuery("from Include").list();
				for (Include include : in) {
					Hibernate.initialize(include.getIdUser());
					if (include.getIdEvent() == a.getIdEvent()) {
						result.add(include);
					}
				}
				for (Include i : result)
					session.delete(i);
				session.delete(a);
			}
		} catch (Exception e) {
			session.getTransaction().rollback();
			return -1;
		}
		session.getTransaction().commit();
		return 0;
	}

	public int makeInclude(String login, String eventName) {
		User user = getUserByLogin(login);
		Event event = getEventByName(eventName);
		if ((user == null) || (event == null))
			return -1;
		else {

			Session session = InitHibernate.getSessionFactory()
					.getCurrentSession();
			session.beginTransaction();
			try {
				session.save(new Include(event.getIdEvent(),
						user.getIdUser()));
			} catch (Exception e) {
				session.getTransaction().rollback();
				return -1;
			}
			session.getTransaction().commit();
			return 0;
		}
	}

	public int deleteInclude(String login, String eventName) {
		User user = getUserByLogin(login);
		Event event = getEventByName(eventName);
		if ((user == null) || (event == null))
			return -1;
		else {
			Session session = InitHibernate.getSessionFactory()
					.getCurrentSession();
			session.beginTransaction();
			try {
				session.delete(new Include(event.getIdEvent(), user.getIdUser()));
			} catch (Exception e) {
				session.getTransaction().rollback();
				return -1;
			}
			session.getTransaction().commit();
			return 0;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Include> getIncludeByLogin(String login) {
		User user = getUserByLogin(login);
		if (user == null)
			return null;
		else {
			List<Include> in;
			List<Include> result = Arrays.asList();
			Session session = InitHibernate.getSessionFactory()
					.getCurrentSession();
			session.beginTransaction();
			try {
				in = session.createQuery("from Include").list();
				for (Include include : in) {
					Hibernate.initialize(include.getIdUser());
					if (include.getIdUser() == user.getIdUser()) {
						result.add(include);
					}
				}
			} catch (Exception e) {
				session.getTransaction().rollback();
				return null;
			}
			session.getTransaction().commit();
			return result;

		}
	}

	@SuppressWarnings("unchecked")
	public List<Include> getIncludeByEvent(String eventName) {
		Event event = getEventByName(eventName);
		if (event == null)
			return null;
		else {
			List<Include> in;
			List<Include> result = Arrays.asList();
			Session session = InitHibernate.getSessionFactory()
					.getCurrentSession();
			session.beginTransaction();
			try {
				in = session.createQuery("from Include").list();
				for (Include include : in) {
					Hibernate.initialize(include.getIdUser());
					if (include.getIdEvent() == event.getIdEvent()) {
						result.add(include);
					}
				}
			} catch (Exception e) {
				session.getTransaction().rollback();
				return null;
			}
			session.getTransaction().commit();
			return result;

		}
	}

	public int deleteInclude(List<Include> list) {
		Session session = InitHibernate.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			for (Include a : list) {
				session.delete(a);
			}
		} catch (Exception e) {
			session.getTransaction().rollback();
			return -1;
		}
		session.getTransaction().commit();
		return 0;
	}
}
