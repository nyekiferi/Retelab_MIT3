package hu.bme.mit.yakindu.analysis.workhere;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.junit.Test;
import org.yakindu.sct.model.sgraph.State;
import org.yakindu.sct.model.sgraph.Statechart;
import org.yakindu.sct.model.sgraph.Transition;

import hu.bme.mit.model2gml.Model2GML;
import hu.bme.mit.yakindu.analysis.modelmanager.ModelManager;

public class Main {
	@Test
	public void test() {
		main(new String[0]);
	}
	
	static Integer number = 1;
	
	public static void main(String[] args) {
		ModelManager manager = new ModelManager();
		Model2GML model2gml = new Model2GML();
		
		// Loading model
		EObject root = manager.loadModel("model_input/example.sct");
		
		// Reading model
		Statechart s = (Statechart) root;
		TreeIterator<EObject> iterator = s.eAllContents();
		while (iterator.hasNext()) {
			EObject content = iterator.next();
			if(content instanceof State) {
				State state = (State) content;
				System.out.println(state.getName());
			}
		}
		
		TreeIterator<EObject> iterator2 = s.eAllContents();
		while (iterator2.hasNext()) {
			EObject content = iterator2.next();
			if(content instanceof Transition) {
				Transition transition = (Transition) content;
				System.out.println(transition.getSource().getName() + " -> " + transition.getTarget().getName());
			}
		}
		
		System.out.println("It's a trap");
		TreeIterator<EObject> iterator3 = s.eAllContents();
		while (iterator3.hasNext()) {
			EObject content = iterator3.next();
			if(content instanceof State) {
				State state = (State) content;
				if(state.getOutgoingTransitions().isEmpty()) {
					System.out.println(state.getName());
				}
			}
		}
		
		TreeIterator<EObject> iterator4 = s.eAllContents();
		while (iterator4.hasNext()) {
			EObject content = iterator4.next();
			if(content instanceof State) {
				State state = (State) content;
				if(state.getName().equals("")) {
					TreeIterator<EObject> iterator5 = s.eAllContents();
					while (iterator5.hasNext()) {
						EObject content1 = iterator5.next();
						if(content1 instanceof State) {
							State state1 = (State) content1;
							if(state1.getName().contains("State-")) {
								String stateNum[] = state1.getName().split("-");
								if(Integer.parseInt(stateNum[1]) >= number) {
									number = Integer.parseInt(stateNum[1]) + 1;
								}
							}
						}
					}
					System.out.println("Suggested name: State-" + number.toString());
					number += 1;
				}
			}
		}
		
		// Transforming the model into a graph representation
		String content = model2gml.transform(root);
		// and saving it
		manager.saveFile("model_output/graph.gml", content);
	}
}
