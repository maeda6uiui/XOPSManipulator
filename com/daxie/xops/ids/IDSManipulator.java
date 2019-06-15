package com.daxie.xops.ids;

import java.io.FileNotFoundException;

import com.daxie.log.LogFile;
import com.daxie.tool.ExceptionFunctions;
import com.daxie.xops.weapon.WeaponData;

/**
 * Manipulates an IDS file.
 * @author Daba
 *
 */
public class IDSManipulator {
	private WeaponData weapon_data=null;
	
	/**
	 * 
	 * @param ids_filename IDS filename to load
	 * @throws FileNotFoundException IDS file not found
	 */
	public IDSManipulator(String ids_filename) throws FileNotFoundException{
		IDSParser ids_parser=new IDSParser(ids_filename);
		weapon_data=ids_parser.GetWeaponData();
	}
	public IDSManipulator() {
		
	}
	
	/**
	 * Returns weapon data.
	 * @return Weapon data
	 */
	public WeaponData GetWeaponData() {
		return new WeaponData(weapon_data);
	}
	/**
	 * Sets weapon data.
	 * @param weapon_data Weapon data
	 */
	public void SetWeaponData(WeaponData weapon_data) {
		this.weapon_data=weapon_data;
	}
	
	/**
	 * Writes out data to an IDS file.
	 * @param ids_filename Filename
	 */
	public void Write(String ids_filename) {
		IDSWriter ids_writer=new IDSWriter(weapon_data);
		try {
			ids_writer.Write(ids_filename);
		}
		catch(FileNotFoundException e) {
			LogFile.WriteFatal("[IDSManipulator-Write] Failed to write data. Below is the stack trace.");
			
			String str=ExceptionFunctions.GetPrintStackTraceString(e);
			LogFile.WriteLine(str);
			
			System.exit(1);
		}
	}
}
