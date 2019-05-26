package com.daxie.xops;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import com.daxie.basis.vector.Vector;
import com.daxie.tool.ByteFunctions;

/**
 * Write data to a PD1 file.
 * @author Daba
 *
 */
class PD1Writer {
	private List<Point> points;
	
	public PD1Writer(List<Point> points) {
		this.points=points;
	}
	
	public void Write(String pd1_filename) throws FileNotFoundException{
		if(points==null)return;
		
		DataOutputStream dos;
		dos=new DataOutputStream(
				new BufferedOutputStream(
						new FileOutputStream(pd1_filename)));
		
		try {
			//Number of points
			int point_num=points.size();
			byte lower_byte=(byte)point_num;
			byte higher_byte=(byte)(point_num>>8);
			dos.writeByte(lower_byte);
			dos.writeByte(higher_byte);
			
			//Point data
			for(int i=0;i<points.size();i++) {
				Point point=points.get(i);
				
				Vector position=point.GetPosition();
				float rotation=point.GetRotation();
				int[] parameters=point.GetParameters();
				
				byte[] b;
				
				//Position
				b=ByteFunctions.float_to_byte_le(position.GetX());
				dos.write(b);
				b=ByteFunctions.float_to_byte_le(position.GetY());
				dos.write(b);
				b=ByteFunctions.float_to_byte_le(position.GetZ());
				dos.write(b);
				
				//Rotation
				b=ByteFunctions.float_to_byte_le(rotation);
				dos.write(b);
				
				//Parameters
				byte btemp;
				for(int j=0;j<4;j++) {
					btemp=(byte)parameters[j];
					dos.writeByte(btemp);
				}
			}
			
			dos.flush();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				dos.close();
			}
			catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
}
