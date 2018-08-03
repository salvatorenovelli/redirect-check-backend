package com.github.snovelli;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.nio.file.Path;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


public class FileSystemStorageServiceTest {

    @Rule
    public TemporaryFolder tmpFolder = new TemporaryFolder();
    private FileSystemStorageService sut;

    @Before
    public void setUp() throws Exception {
        sut = new FileSystemStorageService(tmpFolder.getRoot().toPath());
        sut.init();
    }

    @Test
    public void shouldReturnEmptyStreamWithNoFiles() throws Exception {
        assertThat(sut.loadAll("USER_ID").count(), is(0L));
    }

    @Test
    public void shouldReturnFilesIfPresent() throws Exception {
        getUserFolderFor("USER_1").toFile().createNewFile();
        assertThat(sut.loadAll("USER_1").count(), is(1L));
    }

    private Path getUserFolderFor(String user_1) {
        return tmpFolder.getRoot().toPath().resolve("storage").resolve(user_1);
    }
}