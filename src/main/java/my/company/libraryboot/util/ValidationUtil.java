package my.company.libraryboot.util;

import lombok.experimental.UtilityClass;
import my.company.libraryboot.exception.AppException;
import my.company.libraryboot.model.BaseEntity;

@UtilityClass
public class ValidationUtil {

    public static void checkNew(BaseEntity entity) {
        if (!entity.isNew()) {
            throw new AppException.IllegalRequestDataException(entity.getClass().getSimpleName() + " must be new (id=null)");
        }
    }

    //  Conservative when you reply, but accept liberally (http://stackoverflow.com/a/32728226/548473)
    public static void assureIdConsistent(BaseEntity entity, int id) {
        if (entity.isNew()) {
            entity.setId(id);
        } else if (entity.id() != id) {
            throw new AppException.IllegalRequestDataException(entity.getClass().getSimpleName() + " must have id=" + id);
        }
    }
}