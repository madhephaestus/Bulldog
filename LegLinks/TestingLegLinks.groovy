import com.neuronrobotics.bowlerstudio.vitamins.Vitamins;
import eu.mihosoft.vrl.v3d.parametrics.*;
import com.neuronrobotics.bowlerstudio.creature.ICadGenerator;
import com.neuronrobotics.bowlerstudio.creature.CreatureLab;
import org.apache.commons.io.IOUtils;
import com.neuronrobotics.bowlerstudio.vitamins.*;
import javafx.scene.paint.Color;
import eu.mihosoft.vrl.v3d.Transform;
import com.neuronrobotics.bowlerstudio.physics.TransformFactory;

def remoteLegPiece = ScriptingEngine.gitScriptRun(
            "https://github.com/DotSlash-CTF/Bulldog.git",
            "LegLinks/LegMethods.groovy",
            null
            );

CSG servo = com.neuronrobotics.bowlerstudio.vitamins.Vitamins
.get( "hobbyServo","towerProMG91")
            
CSG horn = com.neuronrobotics.bowlerstudio.vitamins.Vitamins
.get( "hobbyServoHorn","standardMicro1")

//shortest is 50, largest is 180
int length = 80
ArrayList<CSG> shoulder = remoteLegPiece.createShoulder(servo, length)

ArrayList<CSG> thigh = remoteLegPiece.createThigh(servo, horn, length)

ArrayList<CSG> rotatedLink = remoteLegPiece.rotatedLegLink(servo, horn, length)
//rotatedLink.get(0) = rotatedLink.get(0).movez(50)

CSG connector = remoteLegPiece.createConnector(servo, horn, length)
conector = connector.movez(30)
connector = connector.makeKeepaway(-2)
					
for(int i = 0; i < thigh.size(); i++)
{
	thigh.get(i).setManufactuing({CSG arg0 ->
								return arg0.toZMin();
			})
}
for(int i = 0; i < shoulder.size(); i++)
{
	shoulder.get(i).setManufactuing({CSG arg0 ->
								return arg0.toZMin();
			})
}
for(int i = 0; i < rotatedLink.size(); i++)
{
	rotatedLink.get(i).setManufactuing({CSG arg0 ->
								return arg0.toZMin();
			})
}
connector.setManufactuing({CSG arg0 ->
						return arg0.toZMin();
			})
			


ArrayList<CSG> totalParts = thigh ;
totalParts.add(connector)
for(int i = 0; i < shoulder.size(); i++)
{
	//make smaller by nozzle diameter x 2, 0.5*2 makeKeepaway -- createThigh
	totalParts.add(shoulder.get(i))
}
for(int i = 0; i < rotatedLink.size(); i++)
{
	totalParts.add(rotatedLink.get(i))
}
return totalParts//[horn.movey(100).movex(-20), shoulder, thigh, connector]
