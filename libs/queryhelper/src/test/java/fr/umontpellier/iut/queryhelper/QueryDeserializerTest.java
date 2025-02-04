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
public class QueryDeserializerTest {
    @Mock
    private Gson mockJson;

    private QueryDeserializer<TestQuery> serializer;

    private String json;

    private TestQuery expectedQuery;

    @BeforeEach
    void setUp() {
        json = "{\"type\":\"SINGLE\",\"code\":\"INFO\",\"action\":\"Hello World!\"}";


        expectedQuery = new TestQuery(QueryType.SINGLE, QueryCode.INFO, "Hello World!");

        serializer = new QueryDeserializer<>(mockJson, TestQuery.class);
    }

    @Test
    public void deserializeShouldCallJsonDeserialize() {
        serializer.deserialize(json);

        verify(mockJson).fromJson(json, TestQuery.class);
    }

    @Test
    public void deserializeShouldReturnJsonString() {
        when(mockJson.fromJson(json, TestQuery.class)).thenReturn(expectedQuery);

        TestQuery query = serializer.deserialize(json);

        assertEquals(expectedQuery, query);
    }
}
