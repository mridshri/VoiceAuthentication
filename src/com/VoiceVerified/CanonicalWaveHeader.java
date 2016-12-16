package com.VoiceVerified;

import java.nio.ByteBuffer;

import android.util.Log;

public class CanonicalWaveHeader {
	
	
	public byte[] createHeader(){
		byte[] byteParameter;
		byte[] byteArray = new byte[44]; 
		ByteBuffer bb = ByteBuffer.allocate(44);
		
		//WAV header fields of 4 sec wav file.
		bb.put("RIFF".getBytes());  //chunk descriptor
		byteParameter = toLittleEndian(77904, 4);//Chunk Size
		bb.put(byteParameter);
		bb.put("WAVE".getBytes()); //Format
		bb.put("fmt ".getBytes()); //SubChunkId
		byteParameter = toLittleEndian(16, 4); //SubChunkSize
		bb.put(byteParameter);
		byteParameter = toLittleEndian(1, 2); //PCMAudioFormat
		bb.put(byteParameter);
		byteParameter = toLittleEndian(1, 2); //MonoChannel
		bb.put(byteParameter);
		byteParameter = toLittleEndian(8000, 4); //Sample Rate
		bb.put(byteParameter); 
		byteParameter = toLittleEndian(16000, 4); //ByteRate
		bb.put(byteParameter);
		byteParameter = toLittleEndian(2, 2); //BlockAlign
		bb.put(byteParameter);
		byteParameter = toLittleEndian(16, 2); //BitsPerSample
		bb.put(byteParameter);
		bb.put("data".getBytes()); //SubChunk2Id
		byteParameter = toLittleEndian(77868, 4); //SizeOfData
		bb.put(byteParameter);
	
		byteArray = bb.array();
		return byteArray;
		
	}
	
	byte[] toLittleEndian(int parameter, int sizeInByte){
		
		if(sizeInByte==4){
			 return new byte[] {
		                (byte)(parameter),
		                (byte)(parameter >>> 8),
		                (byte)(parameter >>> 16),
		                (byte)(parameter>>>24)
			 };
		} else {
			return new byte[] {
	                (byte)parameter,
	                (byte)(parameter >>>8)
	        };	
		}
	}
}
