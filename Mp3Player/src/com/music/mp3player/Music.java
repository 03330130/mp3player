package com.music.mp3player;

import java.io.Serializable;

public class Music implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	/**MP3�б���ÿһ�׸�����*/
	private int id;
	/**����MP3��ַ������*/
	private String mp3IdCode;
	/**����ר����ַ������*/
	private String albumIdCode;
	/***/
	private String tinguIdCode;
	/**���ߵ�̨Ƶ����ַ��*/
	private String channelIdCode;
	/**���ߵ�̨Ƶ����ַ��*/
	private String artistIdCode;
	/**���߰񵥵�ַ��*/
	private String billIdCode;
	
	/**MP3���֣����ܰ�����׺����*/
	private String mp3Name = null;
	/**MP3�����֣���������׺����*/
	private String mp3SimpleName = null;
	/**������*/
	private String singerName = null;
	/**ר����*/
	private String albumName = null;
	/**�����*/
	private String lrcName = null;
	
	/**MP3��ʱ��*/	
	private String mp3Time;
	/**MP3�Ĵ�С*/
	private String mp3Size;
	/**MP3����*/
	private String mp3Sum;
	
	/**MP3·��*/
	private String mp3URL = null;	
	/**���·��*/
	private String lrcURL = null;
	/**����ͼƬ·��*/
	private String singerBigImageURL = null;
	private String singerMediumImageURL = null;
	private String singerSmallImageURL = null;	
		
	public Music() {

	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMp3IdCode() {
		return mp3IdCode;
	}

	public void setMp3IdCode(String mp3IdCode) {
		this.mp3IdCode = mp3IdCode;
	}

	public String getAlbumIdCode() {
		return albumIdCode;
	}

	public void setAlbumIdCode(String albumIdCode) {
		this.albumIdCode = albumIdCode;
	}

	public String getTinguIdCode() {
		return tinguIdCode;
	}

	public void setTinguIdCode(String tinguIdCode) {
		this.tinguIdCode = tinguIdCode;
	}

	public String getChannelIdCode() {
		return channelIdCode;
	}

	public void setChannelIdCode(String channelIdCode) {
		this.channelIdCode = channelIdCode;
	}

	public String getArtistIdCode() {
		return artistIdCode;
	}

	public void setArtistIdCode(String artistIdCode) {
		this.artistIdCode = artistIdCode;
	}

	public String getBillIdCode() {
		return billIdCode;
	}

	public void setBillIdCode(String billIdCode) {
		this.billIdCode = billIdCode;
	}

	public String getMp3Name() {
		return mp3Name;
	}

	public void setMp3Name(String mp3Name) {
		this.mp3Name = mp3Name;
	}

	public String getMp3SimpleName() {
		return mp3SimpleName;
	}

	public void setMp3SimpleName(String mp3SimpleName) {
		this.mp3SimpleName = mp3SimpleName;
	}

	public String getSingerName() {
		return singerName;
	}

	public void setSingerName(String singerName) {
		this.singerName = singerName;
	}

	public String getAlbumName() {
		return albumName;
	}

	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}

	public String getLrcName() {
		return lrcName;
	}

	public void setLrcName(String lrcName) {
		this.lrcName = lrcName;
	}

	public String getMp3Time() {
		return mp3Time;
	}

	public void setMp3Time(String mp3Time) {
		this.mp3Time = mp3Time;
	}

	public String getMp3Size() {
		return mp3Size;
	}

	public void setMp3Size(String mp3Size) {
		this.mp3Size = mp3Size;
	}

	public String getMp3Sum() {
		return mp3Sum;
	}

	public void setMp3Sum(String mp3Sum) {
		this.mp3Sum = mp3Sum;
	}

	public String getMp3URL() {
		return mp3URL;
	}

	public void setMp3URL(String mp3url) {
		this.mp3URL = mp3url;
	}

	public String getLrcURL() {
		return lrcURL;
	}

	public void setLrcURL(String lrcURL) {
		this.lrcURL = lrcURL;
	}

	public String getSingerBigImageURL() {
		return singerBigImageURL;
	}

	public void setSingerBigImageURL(String singerBigImageURL) {
		this.singerBigImageURL = singerBigImageURL;
	}

	public String getSingerMediumImageURL() {
		return singerMediumImageURL;
	}

	public void setSingerMediumImageURL(String singerMediumImageURL) {
		this.singerMediumImageURL = singerMediumImageURL;
	}

	public String getSingerSmallImageURL() {
		return singerSmallImageURL;
	}

	public void setSingerSmallImageURL(String singerSmallImageURL) {
		this.singerSmallImageURL = singerSmallImageURL;
	}
}
