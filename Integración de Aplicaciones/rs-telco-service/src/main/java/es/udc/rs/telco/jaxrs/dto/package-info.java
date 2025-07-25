@XmlSchema(namespace = "http://ws.udc.es/telco/xml", elementFormDefault = jakarta.xml.bind.annotation.XmlNsForm.QUALIFIED)
@XmlJavaTypeAdapters({
        @XmlJavaTypeAdapter(type= LocalDateTime.class, value= LocalDateTimeXmlAdapter.class)
})
@XmlAccessorType(XmlAccessType.FIELD)

package es.udc.rs.telco.jaxrs.dto;
import es.udc.rs.telco.jaxrs.jaxb.LocalDateTimeXmlAdapter;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlSchema;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapters;

import java.time.LocalDateTime;
