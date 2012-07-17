package com.msxt.examination;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.jboss.seam.transaction.TransactionPropagation;
import org.jboss.seam.transaction.Transactional;

import com.msxt.model.ExaminationCatalog;
import com.msxt.model.Examination;
import com.msxt.model.ExaminationQuestion;
import com.msxt.model.Position;
import com.msxt.model.Question;

@Stateful
@javax.enterprise.inject.Model
@javax.enterprise.context.RequestScoped
public class ExamCreator {
	@PersistenceContext
	private EntityManager em;
	
	private String positionId;

	private Position selectedPosition;
	
	private final Examination newExam = new Examination();
	
	private String catalogsXML;
	
	public String getPositionId() {
		return positionId;
	}

	public void setPositionId(String positionId) {
		this.positionId = positionId;
		selectedPosition = em.find( Position.class, positionId );
	}

	public Position getSelectedPosition() {
		return selectedPosition;
	}

	public Examination getNewExam() {
		return newExam;
	}

	public String getCatalogsXML() {
		return catalogsXML;
	}

	public void setCatalogsXML(String catalogsXML) {
		this.catalogsXML = catalogsXML;
	}
	
	@Transactional(TransactionPropagation.REQUIRED)
	public String create() throws XMLStreamException {
		Position p = em.find( Position.class, positionId );
		newExam.setPosition( p );
		em.persist( newExam );
		
		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		setIfSupported(inputFactory, XMLInputFactory.IS_VALIDATING, Boolean.FALSE);
        setIfSupported(inputFactory, XMLInputFactory.SUPPORT_DTD, Boolean.FALSE);
        
        StringReader sr = new StringReader(catalogsXML);
        final XMLStreamReader streamReader = inputFactory.createXMLStreamReader( sr );
        int catalogaIndex = 0;
        while ( streamReader.hasNext() ) {
            switch ( streamReader.next() ) {
                case XMLStreamConstants.START_ELEMENT: {
                    final Element element = Element.of( streamReader.getName() );
                    switch (element) {
                        case CATALOG: {
                        	parseCatalogElement( streamReader, newExam, catalogaIndex++ );
                            break;
                        }
                        case CATALOGS: {
                            break;
                        }
                        default: {
                            throw unexpectedContent( streamReader );
                        }
                    }
                    break;
                } 
                case XMLStreamConstants.END_DOCUMENT: {
                	break;
                }
            }
        }
        
		return "search";
	}
	
	private void setIfSupported(XMLInputFactory inputFactory, String property, Object value) {
        if (inputFactory.isPropertySupported(property)) {
            inputFactory.setProperty(property, value);
        }
    }
	
	private void parseCatalogElement(final XMLStreamReader reader, Examination exam, int catalogaIndex) throws XMLStreamException {
        ExaminationCatalog ec = new ExaminationCatalog();
        ec.setExam( exam );
        ec.setIndex( catalogaIndex );
        int index=0;
		while (reader.hasNext()) {
		    switch ( reader.nextTag() ) {
		        case XMLStreamConstants.START_ELEMENT: {
		            final Element element = Element.of(reader.getName());
		            switch (element) {
		                case NAME: {
		                	ec.setName( reader.getElementText() );
		                	break;
		                } 
		                case DESC: {
		                    ec.setDescription( reader.getElementText() );
		                    break;
		                } 
		                case QUESTIONS: {
		                	em.persist( ec );
		                	break;
		                } 
		                case QUESTION: {
		                	parseQuestion( reader, ec, index++ );
		                	break;
		                } default: {
		                    throw unexpectedContent(reader);
		                }
		            }
		            break;
		        }
		        case XMLStreamConstants.END_ELEMENT: {
		        	final Element element = Element.of(reader.getName());
		        	if( element==Element.CATALOG )
		        		return;
		        }
		    }
		}
    }
	
	private void parseQuestion(final XMLStreamReader reader, ExaminationCatalog ec, int index) throws XMLStreamException {
    	ExaminationQuestion eq = new ExaminationQuestion();
    	eq.setCatalog( ec );
    	eq.setIndex( index );
    	
		while (reader.hasNext()) {
		    switch ( reader.nextTag() ) {
		        case XMLStreamConstants.START_ELEMENT: {
		            final Element element = Element.of(reader.getName());
		            switch (element) {
		                case ID: {
		                	String qid = reader.getElementText();
		                	Question q = em.find( Question.class, qid );
		                	eq.setQuestion( q );
		                	break;
		                } 
		                case SCORE: {
		                    int score = Integer.valueOf(reader.getElementText());
		                    eq.setScore( score );
		                    break;
		                } 
		                default: {
		                    throw unexpectedContent(reader);
		                }
		            }
		            break;
		        }
		        case XMLStreamConstants.END_ELEMENT: {
		        	final Element element = Element.of(reader.getName());
		        	if( element==Element.QUESTION ) {
		        		em.persist( eq );
		        		return;
		        	}	
		        }
		    }
		}
	}
	
	enum Element {
		CATALOGS,
		CATALOG,
		NAME,
		DESC,
		QUESTIONS,
		QUESTION,
		ID,
		SCORE,
	    // default unknown element
		UNKNOWN;

	    private static final Map<String, Element> elements;

        static {
            Map<String, Element> elementsMap = new HashMap<String, Element>();
            elementsMap.put("catalogs", Element.CATALOGS);
            elementsMap.put("catalog", Element.CATALOG);
            elementsMap.put("name", Element.NAME);
            elementsMap.put("desc", Element.DESC);
            elementsMap.put("questions", Element.QUESTIONS);
            elementsMap.put("question", Element.QUESTION);
            elementsMap.put("id", Element.ID);
            elementsMap.put("score", Element.SCORE);
            elements = elementsMap;
        }

        static Element of(QName qName) {
            final Element element = elements.get(qName.getLocalPart());
            return element == null ? UNKNOWN : element;
        }
	}
	
	
	
	private XMLStreamException unexpectedContent(final XMLStreamReader reader) {
        final String kind;
        switch (reader.getEventType()) {
            case XMLStreamConstants.ATTRIBUTE: kind = "attribute"; break;
            case XMLStreamConstants.CDATA: kind = "cdata"; break;
            case XMLStreamConstants.CHARACTERS: kind = "characters"; break;
            case XMLStreamConstants.COMMENT: kind = "comment"; break;
            case XMLStreamConstants.DTD: kind = "dtd"; break;
            case XMLStreamConstants.END_DOCUMENT: kind = "document end"; break;
            case XMLStreamConstants.END_ELEMENT: kind = "element end"; break;
            case XMLStreamConstants.ENTITY_DECLARATION: kind = "entity declaration"; break;
            case XMLStreamConstants.ENTITY_REFERENCE: kind = "entity ref"; break;
            case XMLStreamConstants.NAMESPACE: kind = "namespace"; break;
            case XMLStreamConstants.NOTATION_DECLARATION: kind = "notation declaration"; break;
            case XMLStreamConstants.PROCESSING_INSTRUCTION: kind = "processing instruction"; break;
            case XMLStreamConstants.SPACE: kind = "whitespace"; break;
            case XMLStreamConstants.START_DOCUMENT: kind = "document start"; break;
            case XMLStreamConstants.START_ELEMENT: kind = "element start"; break;
            default: kind = "unknown"; break;
        }
        final StringBuilder b = new StringBuilder("Unexpected content of type '").append(kind).append('\'');
        if (reader.hasName()) {
            b.append(" named '").append(reader.getName()).append('\'');
        }
        if (reader.hasText()) {
            b.append(", text is: '").append(reader.getText()).append('\'');
        }
        return new XMLStreamException(b.toString(), reader.getLocation());
    }
}
