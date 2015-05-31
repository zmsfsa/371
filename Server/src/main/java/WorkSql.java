package main.java;

import java.util.ArrayList;
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

	@SuppressWarnings("unchecked")
	public User getUserByLogin(String login) {
		User result = null;
		Session session = InitHibernate.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			List<User> list = session.createQuery("from User").list();
			for (User a : list) {
				Hibernate.initialize(a.getLogin());
				if (a.getLogin().equals(login)) {
					result = a;
				}
			}
		} catch (Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
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
		for (User a : user)
			deleteFriendsOf(a.getIdUser());
		Session session = InitHibernate.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			if (user.isEmpty())
				System.out.println("EMPTY");
			else {
				for (User a : user) {
					ArrayList<Include> result = new ArrayList<Include>();
					List<Include> in = session.createQuery("from Include")
							.list();
					for (Include include : in) {
						Hibernate.initialize(include.getIdUser());
						if (include.getIdUser() == a.getIdUser()) {
							result.add(include);
						}
					}
					for (Include i : result) {
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

	public Event getEvent(int idEvent) {
		Event event;
		Session session = InitHibernate.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			event = (Event) session.load(Event.class, idEvent);
			Hibernate.initialize(event.getNameEvent());
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
				ArrayList<Include> result = new ArrayList<Include>();
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

			Include check = getInclude(login, eventName);
			if (check != null)
				return -1;
			Session session = InitHibernate.getSessionFactory()
					.getCurrentSession();
			session.beginTransaction();
			try {
				session.save(new Include(event.getIdEvent(), user.getIdUser(),
						"0", "0"));
			} catch (Exception e) {
				session.getTransaction().rollback();
				return -1;
			}
			session.getTransaction().commit();
			return 0;
		}
	}

	public int checkIn(String login, String eventName, String width,
			String height) {
		User user = getUserByLogin(login);
		Event event = getEventByName(eventName);
		if ((user == null) || (event == null))
			return -1;
		else {

			if (deleteInclude(login, eventName) < 0)
				return -1;/*
						 * Session session = InitHibernate.getSessionFactory()
						 * .getCurrentSession(); session.beginTransaction(); try
						 * { session.update(new Include(login, eventName,
						 * height, width)); } catch (Exception e) {
						 * session.getTransaction().rollback();
						 * System.out.println
						 * ("HELLO I AM PROBLEM FROM CHECK IN"); return -1; }
						 * session.getTransaction().commit();
						 */
			Include in = new Include(event.getIdEvent(), user.getIdUser(),
					height, width);
			Session session = InitHibernate.getSessionFactory()
					.getCurrentSession();
			session.beginTransaction();
			try {
				session.save(in);
			} catch (Exception e) {
				session.getTransaction().rollback();
				System.out.println("HELLO I AM PROBLEM FROM CHECK IN");
				return -1;
			}
			session.getTransaction().commit();
			return 0;
		}
	}

	public int deleteInclude(String login, String eventName) {
		Include del = getInclude(login, eventName);
		if (del == null)
			return -1;

		Session session = InitHibernate.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			session.delete(del);
		} catch (Exception e) {
			session.getTransaction().rollback();
			return -1;
		}
		session.getTransaction().commit();
		return 0;
	}

	public Include getInclude(String login, String eventName) {
		User user = getUserByLogin(login);
		Event event = getEventByName(eventName);
		if ((user == null) || (event == null))
			return null;

		List<Include> inL = getIncludeByLogin(login);
		List<Include> inE = getIncludeByEvent(eventName);
		Include del = null;
		for (Include iL : inL) {
			for (Include iE : inE) {
				if (iL.getIdUser() == iE.getIdUser())
					del = iL;
			}
		}
		return del;
	}

	@SuppressWarnings("unchecked")
	public List<Include> getIncludeByLogin(String login) {
		User user = getUserByLogin(login);
		if (user == null)
			return null;
		else {
			List<Include> in;
			ArrayList<Include> result = new ArrayList<Include>();
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
			ArrayList<Include> result = new ArrayList<Include>();
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

	public int addFriend(Friend friends) {
		Session session = InitHibernate.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			session.save(friends);
		} catch (Exception e) {
			session.getTransaction().rollback();
			System.out.println("HELLO I AM A PROBLEM FROM FRIENDS");
			return -1;
		}
		session.getTransaction().commit();
		return 0;
	}

	public int deleteFriend(Friend friends) {
		Session session = InitHibernate.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			session.delete(friends);
		} catch (Exception e) {
			session.getTransaction().rollback();
			return -1;
		}
		session.getTransaction().commit();
		return 0;
	}

	public int updateFriend(Friend friends) {
		Session session = InitHibernate.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			session.saveOrUpdate(friends);
		} catch (Exception e) {
			session.getTransaction().rollback();
			return -1;
		}
		session.getTransaction().commit();
		return 0;
	}

	@SuppressWarnings("unchecked")
	public List<User> getFriendsOf(int id) {
		User user = getUser(id);
		if (user == null)
			return null;
		else {
			List<Friend> in;
			ArrayList<User> result = new ArrayList<User>();
			Session session = InitHibernate.getSessionFactory()
					.getCurrentSession();
			session.beginTransaction();
			try {
				in = session.createQuery("from Friend").list();
				for (Friend fr : in) {
					Hibernate.initialize(fr.getIdFirst());

					if (fr.getIdFirst() == user.getIdUser()) {
						User u = (User) session.load(User.class,
								fr.getIdSecond());
						Hibernate.initialize(u.getLogin());
						result.add(u);
					}

					if (fr.getIdSecond() == user.getIdUser()) {
						User u = (User) session.load(User.class,
								fr.getIdFirst());
						Hibernate.initialize(u.getLogin());
						result.add(u);
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
	public int deleteFriendsOf(int id) {
		Session session = InitHibernate.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			List<Friend> in = session.createQuery("from Friend").list();
			for (Friend fr : in) {
				Hibernate.initialize(fr.getIdFirst());

				if ((fr.getIdFirst() == id) || (fr.getIdSecond() == id)) {
					session.delete(fr);
				}
			}
		} catch (Exception e) {
			session.getTransaction().rollback();
			return -1;
		}
		session.getTransaction().commit();
		return 0;
	}

	public int addPhoto(Photo photo, String login) {
		int result;
		Session session = InitHibernate.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			result = (int) session.save(photo);
		} catch (Exception e) {
			session.getTransaction().rollback();
			System.out.println("HELLO I AM A PROBLEM FROM PHOTOS");
			e.printStackTrace();
			return -1;
		}
		session.getTransaction().commit();
		if (photo.getIdEvent() == 0) {
			User user = getUserByLogin(login);
			if (user.getPhotoId() != 0) {
				ArrayList<Photo> l = new ArrayList<Photo>();
				l.add(getPhoto(user.getPhotoId()));
				deletePhoto(l);
			}
			user.setPhotoId(result);
			updateUser(user);
		}
		return result;
	}

	public int addPhoto(Photo photo) {
		int result;
		Session session = InitHibernate.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			result = (int) session.save(photo);
		} catch (Exception e) {
			session.getTransaction().rollback();
			System.out.println("HELLO I AM A PROBLEM FROM PHOTOS");
			e.printStackTrace();
			return -1;
		}
		session.getTransaction().commit();
		return 0;
	}

	public Photo getPhoto(int id) {
		Photo photo;
		Session session = InitHibernate.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			photo = (Photo) session.load(Photo.class, id);
			Hibernate.initialize(photo.getIdEvent());
		} catch (Exception e) {
			session.getTransaction().rollback();
			return null;
		}
		session.getTransaction().commit();
		return photo;
	}

	@SuppressWarnings("unchecked")
	public List<Photo> getPhotoByEvent(int id) {
		List<Photo> result = new ArrayList<Photo>();
		Session session = InitHibernate.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			List<Photo> list = session.createQuery("from Photo").list();
			for (Photo a : list) {
				Hibernate.initialize(a.getIdEvent());
				if (a.getIdEvent() == id) {
					result.add(a);
				}
			}
		} catch (Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
			return null;
		}
		session.getTransaction().commit();

		return result;
	}

	public int deletePhoto(List<Photo> list) {

		Session session = InitHibernate.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			for (Photo a : list) {
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
