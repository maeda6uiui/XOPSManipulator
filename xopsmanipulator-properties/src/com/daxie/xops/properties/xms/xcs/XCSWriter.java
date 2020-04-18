package com.daxie.xops.properties.xms.xcs;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.daxie.tool.ByteFunctions;
import com.daxie.xops.properties.XOPSConstants;
import com.daxie.xops.properties.entity.character.CharacterAILevel;
import com.daxie.xops.properties.entity.character.CharacterBinSpecifierAndEnumConverter;
import com.daxie.xops.properties.entity.character.CharacterData;
import com.daxie.xops.properties.entity.character.CharacterModelType;
import com.daxie.xops.properties.entity.character.CharacterTextureType;
import com.daxie.xops.properties.entity.character.CharacterType;

/**
 * Writes data to a XCS file.
 * @author Daba
 *
 */
class XCSWriter {
	private Logger logger=LoggerFactory.getLogger(XCSWriter.class);
	
	private CharacterData[] character_data_array=null;
	
	public XCSWriter(CharacterData[] character_data_array) {
		this.character_data_array=character_data_array;
	}
	
	public int Write(String xcs_filename){
		if(character_data_array==null) {
			logger.warn("Data not prepared.");
			return -1;
		}
		if(character_data_array.length!=XOPSConstants.CHARACTER_NUM) {
			logger.warn("Invalid number of data. data_num={}",character_data_array.length);
			return -1;
		}
		
		try(DataOutputStream dos=new DataOutputStream(new BufferedOutputStream(new FileOutputStream(xcs_filename)))){
			dos.write(0x58);//X
			dos.write(0x43);//C
			dos.write(0x53);//S
			
			dos.write(0x00);
			dos.write(0x01);
			dos.write(0x00);
			dos.write(0x0C);
			dos.write(0x00);
			dos.write(0x2B);
			dos.write(0x00);
			dos.write(0x07);
			dos.write(0x00);
			
			for(int i=0;i<XOPSConstants.CHARACTER_NUM;i++) {
				int itemp;
				byte[] b;
				
				//Texture
				CharacterTextureType texture_type=character_data_array[i].GetTextureType();
				itemp=CharacterBinSpecifierAndEnumConverter.GetBinSpecifierFromCharacterTextureType(texture_type);
				b=ByteFunctions.short_to_byte_le((short)itemp);
				dos.write(b);
				
				//Model
				CharacterModelType model_type=character_data_array[i].GetModelType();
				itemp=CharacterBinSpecifierAndEnumConverter.GetBinSpecifierFromCharacterModelType(model_type);
				b=ByteFunctions.short_to_byte_le((short)itemp);
				dos.write(b);
				
				//HP
				itemp=character_data_array[i].GetHP();
				b=ByteFunctions.ushort_to_byte_le((short)itemp);
				dos.write(b);
				
				//AI level
				CharacterAILevel ai_level=character_data_array[i].GetAILevel();
				itemp=CharacterBinSpecifierAndEnumConverter.GetBinSpecifierFromCharacterAILevel(ai_level);
				b=ByteFunctions.short_to_byte_le((short)itemp);
				dos.write(b);
				
				//Weapon A
				itemp=character_data_array[i].GetWeaponID(0);
				b=ByteFunctions.short_to_byte_le((short)itemp);
				dos.write(b);
				
				//Weapon B
				itemp=character_data_array[i].GetWeaponID(1);
				b=ByteFunctions.short_to_byte_le((short)itemp);
				dos.write(b);
				
				//Type
				CharacterType type=character_data_array[i].GetType();
				itemp=CharacterBinSpecifierAndEnumConverter.GetBinSpecifierFromCharacterType(type);
				b=ByteFunctions.short_to_byte_le((short)itemp);
				dos.write(b);
			}
		}
		catch(IOException e) {
			logger.error("Error while writing.",e);
			return -1;
		}
		
		return 0;
	}
}
