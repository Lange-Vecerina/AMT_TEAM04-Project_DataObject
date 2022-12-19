package org.heig.team04.dataobject;

import com.amazonaws.AmazonServiceException;
import org.heig.team04.dataobject.service.AppServiceS3Impl;
import org.heig.team04.dataobject.service.ServiceInterface;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

class DataObjectAppTests {
    private static final ServiceInterface SERVICE = new AppServiceS3Impl();
    private static final String ROOT_OBJECT = "amt.team04.diduno.education";
    private static final String FOLDER = "testFolder";
    private static final String OBJECT = "testObject";
    private static final String carImageURL = "https://f.vividscreen.info/soft/88446e11f459da29d21aae4923cc8de7/Equus-Bass770-Muscle-Car-square-l.jpg";
    private static final String treeImageURL = "https://m.media-amazon.com/images/I/71LP+MOviUL._AC_UL320_.jpg";
    private static byte[] carImageBytes;
    private static byte[] treeImageBytes;

    @BeforeAll
    public static void setup() throws IOException {
        carImageBytes = Files.readAllBytes(new File("src/test/resources/car.jpg").toPath());
        treeImageBytes = Files.readAllBytes(new File("src/test/resources/tree.jpg").toPath());
    }

    @AfterEach
    public void tearDown() {
        if (SERVICE.exists(ROOT_OBJECT + "/" + OBJECT)) {
            SERVICE.delete(ROOT_OBJECT + "/" + OBJECT, false);
        }

        if (SERVICE.exists(ROOT_OBJECT + "/" + FOLDER)) {
            SERVICE.delete(ROOT_OBJECT + "/" + FOLDER, true);
        }
    }

    @Test
    void exists_RootObjectExists_Exists() {
        // when
        boolean exists = SERVICE.exists(ROOT_OBJECT);

        // then
        assertTrue(exists);
    }

    @Test
    void exists_RootObjectDoesntExist_DoesntExist() {
        // given
        String bsRootObject = "bs.amt.team04.diduno.education";

        // when
        boolean exists = SERVICE.exists(bsRootObject);

        // then
        assertFalse(exists);
    }

    @Test
    void exists_RootObjectAndObjectExist_Exists() {
        // given
        assertTrue(SERVICE.exists(ROOT_OBJECT));
        assertFalse(SERVICE.exists(ROOT_OBJECT + "/" + OBJECT));
        assertDoesNotThrow(() -> SERVICE.create(ROOT_OBJECT + "/" + OBJECT, carImageBytes));

        // when
        boolean exists = SERVICE.exists(ROOT_OBJECT + "/" + OBJECT);
        
        // then
        assertTrue(exists);
    }

    @Test
    void exists_RootObjectExistObjectDoesntExist_DoesntExist() {
        // given
        assertTrue(SERVICE.exists(ROOT_OBJECT));
        assertFalse(SERVICE.exists(ROOT_OBJECT + "/" + OBJECT));
        
        // when
        boolean exists = SERVICE.exists(ROOT_OBJECT + "/" + OBJECT);
        
        // then
        assertFalse(exists);
    }

    @Test
    void create_FromUrl_RootObjectExistsNewObject_Uploaded() {
        // given
        assertTrue(SERVICE.exists(ROOT_OBJECT));
        assertFalse(SERVICE.exists(ROOT_OBJECT + "/" + OBJECT));

        // when
        assertDoesNotThrow(() -> SERVICE.create(ROOT_OBJECT + "/" + OBJECT, carImageURL));

        // then
        assertTrue(SERVICE.exists(ROOT_OBJECT + "/" + OBJECT));
    }

    @Test
    void create_FromUrl_RootObjectExistsObjectAlreadyExists_ThrowException() {
        // given
        assertTrue(SERVICE.exists(ROOT_OBJECT));
        assertFalse(SERVICE.exists(ROOT_OBJECT + "/" + OBJECT));
        assertDoesNotThrow(() -> SERVICE.create(ROOT_OBJECT + "/" + OBJECT, carImageURL));
        assertTrue(SERVICE.exists(ROOT_OBJECT + "/" + OBJECT));

        // when
        Executable executable = () -> SERVICE.create(ROOT_OBJECT + "/" + OBJECT, carImageURL);
        
        // then
        assertThrows(IllegalArgumentException.class, executable);
    }

    @Disabled("This test is disabled because it creates a new root object and we don't want to do that")
    @Test
    void create_FromUrl_RootObjectDoesntExist_Uploaded() {
        // given
        String bsRootObject = "bs.amt.team04.diduno.education";
        assertFalse(SERVICE.exists(bsRootObject));
        assertFalse(SERVICE.exists(bsRootObject + "/" + OBJECT));
        
        // when
        assertDoesNotThrow(() -> SERVICE.create(bsRootObject + "/" + OBJECT, carImageURL));
        
        // then
        assertTrue(SERVICE.exists(bsRootObject));
        assertTrue(SERVICE.exists(bsRootObject + "/" + OBJECT));
    }

    @Test
    void create_FromBytes_RootObjectExistsNewObject_Uploaded() {
        // given
        assertTrue(SERVICE.exists(ROOT_OBJECT));
        assertFalse(SERVICE.exists(ROOT_OBJECT + "/" + OBJECT));
        
        // when
        assertDoesNotThrow(() -> SERVICE.create(ROOT_OBJECT + "/" + OBJECT, carImageBytes));
        
        // then
        assertTrue(SERVICE.exists(ROOT_OBJECT + "/" + OBJECT));
    }

    @Test
    void create_FromBytes_RootObjectExistsObjectAlreadyExists_ThrowException() {
        // given
        assertTrue(SERVICE.exists(ROOT_OBJECT));
        assertFalse(SERVICE.exists(ROOT_OBJECT + "/" + OBJECT));
        assertDoesNotThrow(() -> SERVICE.create(ROOT_OBJECT + "/" + OBJECT, carImageBytes));
        assertTrue(SERVICE.exists(ROOT_OBJECT + "/" + OBJECT));
        
        // when
        Executable executable = () -> SERVICE.create(ROOT_OBJECT + "/" + OBJECT, carImageBytes);
        
        // then
        assertThrows(IllegalArgumentException.class, executable);
    }

    @Disabled("This test is disabled because it creates a new root object and we don't want to do that")
    @Test
    void create_FromBytes_RootObjectDoesntExist_Uploaded() {
        // given
        String bsRootObject = "bs.amt.team04.diduno.education";
        assertFalse(SERVICE.exists(bsRootObject));
        assertFalse(SERVICE.exists(bsRootObject + "/" + OBJECT));
        
        // when
        assertDoesNotThrow(() -> SERVICE.create(bsRootObject + "/" + OBJECT, carImageBytes));
        
        // then
        assertTrue(SERVICE.exists(bsRootObject));
        assertTrue(SERVICE.exists(bsRootObject + "/" + OBJECT));
    }

    @Test
    void update_FromUrl_RootObjectExistsObjectExists_Updated() {
        // given
        assertTrue(SERVICE.exists(ROOT_OBJECT));
        assertFalse(SERVICE.exists(ROOT_OBJECT + "/" + OBJECT));
        assertDoesNotThrow(() -> SERVICE.create(ROOT_OBJECT + "/" + OBJECT, carImageURL));
        assertTrue(SERVICE.exists(ROOT_OBJECT + "/" + OBJECT));

        // when
        assertDoesNotThrow(() -> SERVICE.update(ROOT_OBJECT + "/" + OBJECT, treeImageURL));

        // then
        assertTrue(SERVICE.exists(ROOT_OBJECT + "/" + OBJECT));
    }

    @Test
    void update_FromUrl_RootObjectExistsObjectDoesntExist_ThrowException() {
        // given
        assertTrue(SERVICE.exists(ROOT_OBJECT));
        assertFalse(SERVICE.exists(ROOT_OBJECT + "/" + OBJECT));

        // when
        Executable executable = () -> SERVICE.update(ROOT_OBJECT + "/" + OBJECT, treeImageURL);

        // then
        assertThrows(IllegalArgumentException.class, executable);
    }

    @Test
    void update_FromUrl_RootObjectDoesntExist_ThrowException() {
        // given
        String bsRootObject = "bs.amt.team04.diduno.education";
        assertFalse(SERVICE.exists(bsRootObject));
        assertFalse(SERVICE.exists(bsRootObject + "/" + OBJECT));

        // when
        Executable executable = () -> SERVICE.update(bsRootObject + "/" + OBJECT, treeImageURL);

        // then
        assertThrows(IllegalArgumentException.class, executable);
    }

    @Test
    void update_FromBytes_RootObjectExistsObjectExists_Updated() {
        // given
        assertTrue(SERVICE.exists(ROOT_OBJECT));
        assertFalse(SERVICE.exists(ROOT_OBJECT + "/" + OBJECT));
        assertDoesNotThrow(() -> SERVICE.create(ROOT_OBJECT + "/" + OBJECT, carImageBytes));
        assertTrue(SERVICE.exists(ROOT_OBJECT + "/" + OBJECT));

        // when
        assertDoesNotThrow(() -> SERVICE.update(ROOT_OBJECT + "/" + OBJECT, treeImageBytes));

        // then
        assertTrue(SERVICE.exists(ROOT_OBJECT + "/" + OBJECT));
    }

    @Test
    void update_FromBytes_RootObjectExistsObjectDoesntExist_ThrowException() {
        // given
        assertTrue(SERVICE.exists(ROOT_OBJECT));
        assertFalse(SERVICE.exists(ROOT_OBJECT + "/" + OBJECT));

        // when
        Executable executable = () -> SERVICE.update(ROOT_OBJECT + "/" + OBJECT, treeImageBytes);

        // then
        assertThrows(IllegalArgumentException.class, executable);
    }

    @Test
    void update_FromBytes_RootObjectDoesntExist_ThrowException() {
        // given
        String bsRootObject = "bs.amt.team04.diduno.education";
        assertFalse(SERVICE.exists(bsRootObject));
        assertFalse(SERVICE.exists(bsRootObject + "/" + OBJECT));

        // when
        Executable executable = () -> SERVICE.update(bsRootObject + "/" + OBJECT, treeImageBytes);

        // then
        assertThrows(IllegalArgumentException.class, executable);
    }

    @Test
    void read_ObjectExists_Downloaded() {
        // given
        assertTrue(SERVICE.exists(ROOT_OBJECT));
        assertFalse(SERVICE.exists(ROOT_OBJECT + "/" + OBJECT));
        assertDoesNotThrow(() -> SERVICE.create(ROOT_OBJECT + "/" + OBJECT, carImageBytes));
        assertTrue(SERVICE.exists(ROOT_OBJECT + "/" + OBJECT));
        
        // when
        byte[] bytes = SERVICE.read(ROOT_OBJECT + "/" + OBJECT);
        
        // then
        assertNotNull(bytes);
    }

    @Test
    void read_ObjectDoesntExist_ThrowException() {
        // given
        assertTrue(SERVICE.exists(ROOT_OBJECT));
        assertFalse(SERVICE.exists(ROOT_OBJECT + "/" + OBJECT));
        
        // when
        Executable executable = () -> SERVICE.read(ROOT_OBJECT + "/" + OBJECT);
        
        // then
        assertThrows(IllegalArgumentException.class, executable);
    }

    @Test
    void publish_ObjectExists_Published() {
        // given
        assertTrue(SERVICE.exists(ROOT_OBJECT));
        assertFalse(SERVICE.exists(ROOT_OBJECT + "/" + OBJECT));
        assertDoesNotThrow(() -> SERVICE.create(ROOT_OBJECT + "/" + OBJECT, carImageBytes));
        assertTrue(SERVICE.exists(ROOT_OBJECT + "/" + OBJECT));

        // when
        String url = SERVICE.publish(ROOT_OBJECT + "/" + OBJECT, 10);

        // then
        assertNotNull(url);
    }

    @Test
    void publish_ObjectDoesntExist_ThrowException() {
        // given
        assertTrue(SERVICE.exists(ROOT_OBJECT));
        assertFalse(SERVICE.exists(ROOT_OBJECT + "/" + OBJECT));

        // when
        Executable executable = () -> SERVICE.publish(ROOT_OBJECT + "/" + OBJECT, 10);

        // then
        assertThrows(IllegalArgumentException.class, executable);
    }

    @Test
    void delete_SingleObjectExists_Removed() {
        // given
        assertTrue(SERVICE.exists(ROOT_OBJECT));
        assertFalse(SERVICE.exists(ROOT_OBJECT + "/" + OBJECT));
        assertDoesNotThrow(() -> SERVICE.create(ROOT_OBJECT + "/" + OBJECT, carImageBytes));
        assertTrue(SERVICE.exists(ROOT_OBJECT + "/" + OBJECT));

        // when
        assertDoesNotThrow(() -> SERVICE.delete(ROOT_OBJECT + "/" + OBJECT, false));

        // then
        assertFalse(SERVICE.exists(ROOT_OBJECT + "/" + OBJECT));
    }

    @Test
    void delete_SingleObjectDoesntExist_ThrowException() {
        // given
        assertTrue(SERVICE.exists(ROOT_OBJECT));
        assertFalse(SERVICE.exists(ROOT_OBJECT + "/" + OBJECT));

        // when
        Executable executable = () -> SERVICE.delete(ROOT_OBJECT + "/" + OBJECT, false);

        // then
        assertThrows(IllegalArgumentException.class, executable);
    }

    @Test
    void delete_FolderObjectExistWithoutRecursiveOption_ThrowException() {
        // given
        assertTrue(SERVICE.exists(ROOT_OBJECT));
        assertFalse(SERVICE.exists(ROOT_OBJECT + "/" + FOLDER));
        assertDoesNotThrow(() -> SERVICE.create(ROOT_OBJECT + "/" + FOLDER + "/" + OBJECT, carImageBytes));
        assertTrue(SERVICE.exists(ROOT_OBJECT + "/" + FOLDER + "/" + OBJECT));
        assertTrue(SERVICE.exists(ROOT_OBJECT + "/" + FOLDER));

        // when
        Executable executable = () -> SERVICE.delete(ROOT_OBJECT + "/" + FOLDER, false);

        // then
        assertThrows(AmazonServiceException.class, executable);
        assertTrue(SERVICE.exists(ROOT_OBJECT + "/" + FOLDER));
        assertTrue(SERVICE.exists(ROOT_OBJECT + "/" + FOLDER + "/" + OBJECT));
    }

    @Test
    void delete_FolderObjectExistWithRecursiveOption_Removed() {
        // given
        assertTrue(SERVICE.exists(ROOT_OBJECT));
        assertFalse(SERVICE.exists(ROOT_OBJECT + "/" + FOLDER));
        assertDoesNotThrow(() -> SERVICE.create(ROOT_OBJECT + "/" + FOLDER + "/" + OBJECT, carImageBytes));
        assertTrue(SERVICE.exists(ROOT_OBJECT + "/" + FOLDER + "/" + OBJECT));

        // when
        assertDoesNotThrow(() -> SERVICE.delete(ROOT_OBJECT + "/" + FOLDER, true));

        // then
        assertFalse(SERVICE.exists(ROOT_OBJECT + "/" + FOLDER));
        assertFalse(SERVICE.exists(ROOT_OBJECT + "/" + FOLDER + "/" + OBJECT));
    }

    @Disabled("This test is disabled because it deletes a root object and we don't want to do that")
    @Test
    void delete_RootObjectNotEmptyWithoutRecursiveOption_ThrowException() {
        // given
        assertTrue(SERVICE.exists(ROOT_OBJECT));
        assertFalse(SERVICE.exists(ROOT_OBJECT + "/" + OBJECT));
        assertDoesNotThrow(() -> SERVICE.create(ROOT_OBJECT + "/" + OBJECT, carImageBytes));
        assertTrue(SERVICE.exists(ROOT_OBJECT + "/" + OBJECT));

        // when
        Executable executable = () -> SERVICE.delete(ROOT_OBJECT, false);

        // then
        assertThrows(IllegalArgumentException.class, executable);
    }

    @Disabled("This test is disabled because it deletes a root object and we don't want to do that")
    @Test
    void delete_RootObjectNotEmptyWithRecursiveOption_Removed() {
        // given
        assertTrue(SERVICE.exists(ROOT_OBJECT));
        assertFalse(SERVICE.exists(ROOT_OBJECT + "/" + OBJECT));
        assertDoesNotThrow(() -> SERVICE.create(ROOT_OBJECT + "/" + OBJECT, carImageBytes));
        assertTrue(SERVICE.exists(ROOT_OBJECT + "/" + OBJECT));

        // when
        assertDoesNotThrow(() -> SERVICE.delete(ROOT_OBJECT, true));

        // then
        assertFalse(SERVICE.exists(ROOT_OBJECT));
    }

    @Test
    void delete_RootObjectNotExists_ThrowException() {
        // given
        String bsRootObject = "bs.amt.team04.diduno.education";
        assertFalse(SERVICE.exists(bsRootObject));

        // when
        Executable executable = () -> SERVICE.delete(bsRootObject, false);

        // then
        assertThrows(IllegalArgumentException.class, executable);
    }

}
