package trf.smt.com.netlibrary.greendao;


import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import java.util.Map;

import trf.smt.com.netlibrary.enity.HisContact;
import trf.smt.com.netlibrary.enity.MessageInfo;
import trf.smt.com.netlibrary.enity.Person;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 *
 * @see AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig personDaoConfig;
    private final DaoConfig hisContactDaoConfig;
    private final DaoConfig messageInfoDaoConfig;

    private final PersonDao personDao;
    private final HisContactDao hisContactDao;
    private final MessageInfoDao messageInfoDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        personDaoConfig = daoConfigMap.get(PersonDao.class).clone();
        personDaoConfig.initIdentityScope(type);

        hisContactDaoConfig = daoConfigMap.get(HisContactDao.class).clone();
        hisContactDaoConfig.initIdentityScope(type);

        messageInfoDaoConfig = daoConfigMap.get(MessageInfoDao.class).clone();
        messageInfoDaoConfig.initIdentityScope(type);

        personDao = new PersonDao(personDaoConfig, this);
        hisContactDao = new HisContactDao(hisContactDaoConfig, this);
        messageInfoDao = new MessageInfoDao(messageInfoDaoConfig, this);

        registerDao(Person.class, personDao);
        registerDao(HisContact.class, hisContactDao);
        registerDao(MessageInfo.class, messageInfoDao);
    }

    public void clear() {
        personDaoConfig.clearIdentityScope();
        hisContactDaoConfig.clearIdentityScope();
        messageInfoDaoConfig.clearIdentityScope();
    }

    public PersonDao getPersonDao() {
        return personDao;
    }

    public HisContactDao getHisContactDao() {
        return hisContactDao;
    }

    public MessageInfoDao getMessageInfoDao() {
        return messageInfoDao;
    }

}