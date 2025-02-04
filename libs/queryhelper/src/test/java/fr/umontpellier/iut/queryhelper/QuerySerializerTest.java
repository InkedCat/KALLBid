package fr.umontpellier.iut.queryhelper;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class QuerySerializerTest {


    @Mock
    private Gson mockJson;

    private QuerySerializer<TestQuery> serializer;

    private String expectedjson;

    private TestQuery query;

    @BeforeEach
    void setUp() {
        query = new TestQuery(QueryType.SINGLE, QueryCode.INFO, "Hello World!");

        expectedjson = "{\"type\":\"SINGLE\",\"code\":\"INFO\",\"action\":\"Hello World!\"}";

        serializer = new QuerySerializer<>(mockJson);
    }

    @Test
    public void serializeShouldCallJsonSerialize() {
        serializer.serialize(query);

        verify(mockJson).toJson(query);
    }

    @Test
    public void serializeShouldReturnJsonString() {
        when(mockJson.toJson(query)).thenReturn(expectedjson);

        String json = serializer.serialize(query);

        assertEquals(expectedjson, json);
    }
}
