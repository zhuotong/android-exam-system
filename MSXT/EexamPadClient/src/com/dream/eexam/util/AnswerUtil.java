package com.dream.eexam.util;

import java.util.List;

import com.dream.eexam.model.Answer;

public class AnswerUtil {
	//header message
	private static String EXAM_ANSWER_HEADER = "<examanswer>" +
										"<conversation>%s</conversation>" +
									"<examinationid>%s</examinationid>"+
									"<answers>";
	//tail message
	private static String EXAM_ANSWER_FOOTER = "</examanswer>";
	
	private static String ANSWER_HEADER = "<answer>";
	private static String ANSWER_FOOTER = "</answer>";
	
	private StringBuffer allAnswersBf = new StringBuffer();
	
	public void addAnswer(Answer answer){
		allAnswersBf.append(ANSWER_HEADER);
		allAnswersBf.append("<questionid>"+answer.getQuestionId()+"</questionid>");
		allAnswersBf.append("<content>"+answer.getChoiceIdsString()+"</content>");
		allAnswersBf.append(ANSWER_FOOTER);
		allAnswersBf.append("\n");
	}
	
	public String getAnswers(List<Answer> answerList,String conversation,String examinationid){
		allAnswersBf.append(String.format(EXAM_ANSWER_HEADER, conversation,examinationid));
		if(answerList!=null&&answerList.size()>0){
			for(Answer answer: answerList){
				addAnswer(answer);
			}
		}
		allAnswersBf.append(EXAM_ANSWER_FOOTER);
		return allAnswersBf.toString();
	}
	
}
