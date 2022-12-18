package org.heig.team04.dataobject;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DataObjectAppTests {
    private static String bucketPath;
    private static String folderPath;
    private static String folderPathForAssert;
    private static String carImageURL;
    private static String carImageBytes;
    private static String treeImageURL;
    private static String treeImageBytes;

    @BeforeAll
    public static void setup() {
        bucketPath = "https://s3.eu-west-3.amazonaws.com/heig-vd-2020-team04/";
        folderPath = "dataobject/";
        folderPathForAssert = "dataobject";
        carImageURL = bucketPath + folderPath + "car.jpg";
        carImageBytes = "car.jpg";
        treeImageURL = bucketPath + folderPath + "tree.jpg";
        treeImageBytes = "tree.jpg";
    }

    /*@Test
    void DoesObjectExist_RootObjectExists_Exists() {
        // given
        // when
        // then
    }

    @Test
    void DoesObjectExist_RootObjectDoesntExist_DoesntExist() {
        // given
        // when
        // then
    }

    @Test
    void DoesObjectExist_RootObjectAndObjectExist_Exists() {
        // given
        // when
        // then
    }

    @Test
    void DoesObjectExist_RootObjectExistObjectDoesntExist_DoesntExist() {
        // given
        // when
        // then
    }

    @Test
    void UploadObject_RootObjectExistsNewObject_Uploaded() {
        // given
        // when
        // then
    }

    @Test
    void UploadObject_RootObjectExistsObjectAlreadyExists_ThrowException() {
        // given
        // when
        // then
    }

    @Test
    void UploadObject_RootObjectDoesntExist_Uploaded() {
        // given
        // when
        // then
    }

    @Test
    void DownloadObject_ObjectExists_Downloaded() {
        // given
        // when
        // then
    }

    @Test
    void DownloadObject_ObjectDoesntExist_ThrowException() {
        // given
        // when
        // then
    }

    @Test
    void PublishObject_ObjectExists_Published() {
        // given
        // when
        // then
    }

    @Test
    void PublishObject_ObjectDoesntExist_ThrowException() {
        // given
        // when
        // then
    }

    @Test
    void RemoveObject_SingleObjectExists_Removed() {
        // given
        // when
        // then
    }

    @Test
    void RemoveObject_SingleObjectDoesntExist_ThrowException() {
        // given
        // when
        // then
    }

    @Test
    void RemoveObject_FolderObjectExistWithoutRecursiveOption_ThrowException() {
        // given
        // when
        // then
    }

    @Test
    void RemoveObject_FolderObjectExistWithRecursiveOption_Removed() {
        // given
        // when
        // then
    }

    @Test
    void RemoveObject_RootObjectNotEmptyWithoutRecursiveOption_ThrowException() {
        // given
        // when
        // then
    }

    @Test
    void RemoveObject_RootObjectNotEmptyWithRecursiveOption_Removed() {
        // given
        // when
        // then
    }

    @Test
    void RemoveObject_ObjectNotExists_ThrowException() {
        // given
        // when
        // then
    }*/

}
