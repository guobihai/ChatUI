package trf.smt.com.netlibrary.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.internal.DaoConfig;

import trf.smt.com.netlibrary.enity.HisContact;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * DAO for table "HIS_CONTACT".
 */
public class HisContactDao extends AbstractDao<HisContact, Long> {

    public static final String TABLENAME = "HIS_CONTACT";

    /**
     * Properties of entity HisContact.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "Id", true, "_id");
        public final static Property UserName = new Property(1, String.class, "userName", false, "USER_NAME");
        public final static Property NickName = new Property(2, String.class, "nickName", false, "NICK_NAME");
        public final static Property ConRemark = new Property(3, String.class, "conRemark", false, "CON_REMARK");
        public final static Property IconUrl = new Property(4, String.class, "iconUrl", false, "ICON_URL");
        public final static Property Type = new Property(5, int.class, "type", false, "TYPE");
        public final static Property LastMsg = new Property(6, String.class, "lastMsg", false, "LAST_MSG");
        public final static Property CreateTime = new Property(7, String.class, "createTime", false, "CREATE_TIME");
        public final static Property LoginUserId = new Property(8, String.class, "loginUserId", false, "LOGIN_USER_ID");
        public final static Property OrderCode = new Property(9, int.class, "orderCode", false, "ORDER_CODE");
        public final static Property MsgCount = new Property(10, int.class, "msgCount", false, "MSG_COUNT");
        public final static Property MemberId = new Property(11, int.class, "memberId", false, "MEMBER_ID");
        public final static Property Code = new Property(12, int.class, "code", false, "CODE");
        public final static Property CodeName = new Property(13, String.class, "codeName", false, "CODE_NAME");
    }


    public HisContactDao(DaoConfig config) {
        super(config);
    }

    public HisContactDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /**
     * Creates the underlying database table.
     */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists ? "IF NOT EXISTS " : "";
        db.execSQL("CREATE TABLE " + constraint + "\"HIS_CONTACT\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: Id
                "\"USER_NAME\" TEXT," + // 1: userName
                "\"NICK_NAME\" TEXT," + // 2: nickName
                "\"CON_REMARK\" TEXT," + // 3: conRemark
                "\"ICON_URL\" TEXT," + // 4: iconUrl
                "\"TYPE\" INTEGER NOT NULL ," + // 5: type
                "\"LAST_MSG\" TEXT," + // 6: lastMsg
                "\"CREATE_TIME\" TEXT," + // 7: createTime
                "\"LOGIN_USER_ID\" TEXT," + // 8: loginUserId
                "\"ORDER_CODE\" INTEGER NOT NULL ," + // 9: orderCode
                "\"MSG_COUNT\" INTEGER NOT NULL ," + // 10: msgCode
                "\"MEMBER_ID\" INTEGER NOT NULL ," + // 11: menberId
                "\"CODE\" INTEGER NOT NULL ," + // 12: code
                "\"CODE_NAME\" TEXT);"); // 13: codeName
    }

    /**
     * Drops the underlying database table.
     */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"HIS_CONTACT\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, HisContact entity) {
        stmt.clearBindings();

        Long Id = entity.getId();
        if (Id != null) {
            stmt.bindLong(1, Id);
        }

        String userName = entity.getUserName();
        if (userName != null) {
            stmt.bindString(2, userName);
        }

        String nickName = entity.getNickName();
        if (nickName != null) {
            stmt.bindString(3, nickName);
        }

        String conRemark = entity.getConRemark();
        if (conRemark != null) {
            stmt.bindString(4, conRemark);
        }

        String iconUrl = entity.getIconUrl();
        if (iconUrl != null) {
            stmt.bindString(5, iconUrl);
        }
        stmt.bindLong(6, entity.getType());

        String lastMsg = entity.getLastMsg();
        if (lastMsg != null) {
            stmt.bindString(7, lastMsg);
        }

        String createTime = entity.getCreateTime();
        if (createTime != null) {
            stmt.bindString(8, createTime);
        }

        String loginUserId = entity.getLoginUserId();
        if (loginUserId != null) {
            stmt.bindString(9, loginUserId);
        }
        stmt.bindLong(10, entity.getOrderCode());
        stmt.bindLong(11, entity.getMsgCount());
        stmt.bindLong(12, entity.getMemberId());
        stmt.bindLong(13, entity.getCode());

        String codeName = entity.getCodeName();
        if (codeName != null) {
            stmt.bindString(14, codeName);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, HisContact entity) {
        stmt.clearBindings();

        Long Id = entity.getId();
        if (Id != null) {
            stmt.bindLong(1, Id);
        }

        String userName = entity.getUserName();
        if (userName != null) {
            stmt.bindString(2, userName);
        }

        String nickName = entity.getNickName();
        if (nickName != null) {
            stmt.bindString(3, nickName);
        }

        String conRemark = entity.getConRemark();
        if (conRemark != null) {
            stmt.bindString(4, conRemark);
        }

        String iconUrl = entity.getIconUrl();
        if (iconUrl != null) {
            stmt.bindString(5, iconUrl);
        }
        stmt.bindLong(6, entity.getType());

        String lastMsg = entity.getLastMsg();
        if (lastMsg != null) {
            stmt.bindString(7, lastMsg);
        }

        String createTime = entity.getCreateTime();
        if (createTime != null) {
            stmt.bindString(8, createTime);
        }

        String loginUserId = entity.getLoginUserId();
        if (loginUserId != null) {
            stmt.bindString(9, loginUserId);
        }
        stmt.bindLong(10, entity.getOrderCode());
        stmt.bindLong(11, entity.getMsgCount());
        stmt.bindLong(12, entity.getMemberId());
        stmt.bindLong(13, entity.getCode());

        String codeName = entity.getCodeName();
        if (codeName != null) {
            stmt.bindString(14, codeName);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }

    @Override
    public HisContact readEntity(Cursor cursor, int offset) {
        HisContact entity = new HisContact( //
                cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // Id
                cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // userName
                cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // nickName
                cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // conRemark
                cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // iconUrl
                cursor.getInt(offset + 5), // type
                cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // lastMsg
                cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // createTime
                cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // loginUserId
                cursor.getInt(offset + 9), // orderCode
                cursor.getInt(offset + 10), // msgCount
                cursor.getInt(offset + 11), // menberId
                cursor.getInt(offset + 12), // code
                cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13) // codeName
        );
        return entity;
    }

    @Override
    public void readEntity(Cursor cursor, HisContact entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setUserName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setNickName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setConRemark(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setIconUrl(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setType(cursor.getInt(offset + 5));
        entity.setLastMsg(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setCreateTime(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setLoginUserId(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setOrderCode(cursor.getInt(offset + 9));
        entity.setMsgCount(cursor.getInt(offset + 10));
        entity.setMemberId(cursor.getInt(offset + 11));
        entity.setCode(cursor.getInt(offset + 12));
        entity.setCodeName(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
    }

    @Override
    protected final Long updateKeyAfterInsert(HisContact entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }

    @Override
    public Long getKey(HisContact entity) {
        if (entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(HisContact entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }

}
