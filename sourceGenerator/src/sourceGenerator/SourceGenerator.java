package sourceGenerator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SourceGenerator {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		File path = new File("");
//		System.out.println(path.getAbsolutePath()); //--> 프로젝트 폴더의 주소가 출력됨

		File inFile = new File(path.getAbsolutePath()+"\\src\\sourceGenerator\\file", "in.txt");

		//==========================//
        // 텍스트 파일 읽기
        //==========================//
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(inFile));
            String line;


            Pattern ptn ;
            Matcher matcher ;
            List<String> patternArr = new ArrayList<String>();
            patternArr.add("id");
            patternArr.add("type");
            patternArr.add("text");
            patternArr.add("width");
            patternArr.add("align");
            patternArr.add("required");
            patternArr.add("format");
            patternArr.add("maxLength");

            Map<String,String> inMap = new HashMap<String,String>();
            List<Map<String,String>> inArr = new ArrayList<Map<String, String>>();
            while ((line = br.readLine()) != null) {

            	int headeridx = line.indexOf("<w:dhtmlxgrid.columnHeader");
            	if(headeridx > -1 ) {

            		line = line.replace(" = \"", "=\"");		//key value 간의 공백 제거
            		inMap =  new HashMap<String,String>();
            		for(String patternStr : patternArr) {
            			//id
                		ptn = Pattern.compile(patternStr+"=[\"']([가-힣a-z-A-Z-\\s0-9\\#\\_\\,\\.\\$\\{\\}\\=\\?\\:\\'\\(\\)\\/\\*]*)");
                		matcher = ptn.matcher(line);
                		while(matcher.find()){
                			inMap.put(patternStr, matcher.group(1));
//                			System.out.println(patternStr +"==="+matcher.group(1));
                		}
            		}
            		inArr.add(inMap);
            	}
            }

            boolean editGbn;
            StringBuffer ibsheetStr = new StringBuffer();
            List<String> ibsheetArr = new ArrayList<String>();
            for(Map<String,String> mapstr : inArr) {
            	editGbn = true;
            	
            	ibsheetStr = new StringBuffer();
            	for(String patternStr : patternArr) {
            		if("text" == patternStr) {
            			if("#master_checkbox".equals(mapstr.get(patternStr))) {
                			ibsheetStr.append("{Header:\""+mapstr.get(patternStr) +"\", HeaderCheck:true, ");	
            			}else {
                			ibsheetStr.append("{Header:\""+mapstr.get(patternStr) +"\", HeaderCheck:false,");	
            			}
            		}
            	}
            	
            	for(String patternStr : patternArr) {
            		//System.out.println(mapstr.get(patternStr));

            		if("type".equalsIgnoreCase(patternStr)) {
            			if(mapstr.get(patternStr) != null) {
            				if("ch".equalsIgnoreCase(mapstr.get(patternStr)))
            					ibsheetStr.append("Type:\"CheckBox\", ");
            				else if("ro".equalsIgnoreCase(mapstr.get(patternStr))) {
            					ibsheetStr.append("Type:\"Text\", ");
            					editGbn = false;
            				}else if("ra".equalsIgnoreCase(mapstr.get(patternStr))) {
            					ibsheetStr.append("Type:\"Radio\", ");
            				}else if("ccoro".equalsIgnoreCase(mapstr.get(patternStr))) {
            					ibsheetStr.append("Type:\"Combo\", ComboCode:\"\", ComboText:\"\" , ");
            					editGbn = false;
            				}else if("coro".equalsIgnoreCase(mapstr.get(patternStr))) {
            					ibsheetStr.append("Type:\"Combo\", ComboCode:\"\", ComboText:\"\" , ");
            				}else if("ed".equalsIgnoreCase(mapstr.get(patternStr)))
            					ibsheetStr.append("Type:\"Text\", ");
            				else if("edn".equalsIgnoreCase(mapstr.get(patternStr)))
            					if(mapstr.get("format") != null) {		//format 이 있을 경우  Float
            						
            						ibsheetStr.append("Type:\"Float\", ");
            						
            						if(mapstr.get("format").indexOf("amtFormat") > -1) {
            							ibsheetStr.append("Format:\"#,###\", ");
            						}else if(mapstr.get("format").indexOf("priceFormat") > -1) {
            							ibsheetStr.append("Format:\"#,###\", ");
            						}else if(mapstr.get("format").indexOf("qtyFormat") > -1) {
            							ibsheetStr.append("Format:\"#,##0.###\", ");
            						}else if(mapstr.get("format").indexOf("rateFormat") > -1) {
            							ibsheetStr.append("Format:\"#,##0.##\", ");
            						}else if(mapstr.get("format").indexOf("changeQtyFormat") > -1) {
            							ibsheetStr.append("Format:\"#,##0.#####\", ");
            						}
            						
            					}else {
            						ibsheetStr.append("Type:\"Text\", ");
            					}
            				else if("clink".equalsIgnoreCase(mapstr.get(patternStr))) {
            					ibsheetStr.append("FontUnderline:\"true\", ");
            					ibsheetStr.append("Cursor:\"Pointer\", ");
            					ibsheetStr.append("FontColor:\"Blue\", ");
            					editGbn = false;
            				}else if("edtxt".equalsIgnoreCase(mapstr.get(patternStr))) {
            					ibsheetStr.append("Type:\"Text\", ");
            				}else if("rotxt".equalsIgnoreCase(mapstr.get(patternStr))) {
            					ibsheetStr.append("Type:\"Text\", ");
            					editGbn = false;
            				}else if("img".equalsIgnoreCase(mapstr.get(patternStr))) {
            					ibsheetStr.append("Type:\"Image\", ");
            				}else if("cdatero".equalsIgnoreCase(mapstr.get(patternStr)) || "cdate".equalsIgnoreCase(mapstr.get(patternStr))) {
            					if("cdatero".equalsIgnoreCase(mapstr.get(patternStr))) {
            						editGbn = false;
            					}
            					ibsheetStr.append("Type:\"Date\", ");
            				}else if("ron".equalsIgnoreCase(mapstr.get(patternStr))) {

            					if(mapstr.get("format") != null) {		//format 이 있을 경우  Float
            						
            						ibsheetStr.append("Type:\"Float\", ");
            						
            						if(mapstr.get("format").indexOf("amtFormat") > -1) {
            							ibsheetStr.append("Format:\"#,###\", ");
            						}else if(mapstr.get("format").indexOf("priceFormat") > -1) {
            							ibsheetStr.append("Format:\"#,###\", ");
            						}else if(mapstr.get("format").indexOf("qtyFormat") > -1) {
            							ibsheetStr.append("Format:\"#,##0.###\", ");
            						}else if(mapstr.get("format").indexOf("rateFormat") > -1) {
            							ibsheetStr.append("Format:\"#,##0.##\", ");
            						}else if(mapstr.get("format").indexOf("changeQtyFormat") > -1) {
            							ibsheetStr.append("Format:\"#,##0.#####\", ");
            						}
            						
            					}else {
            						ibsheetStr.append("Type:\"Int\", ");
            					}
            					editGbn = false;
            				}
            				else
            					System.out.println("type check :::: " + mapstr.get(patternStr));			//등록되지 않은 타입 명
            			}else {
            				ibsheetStr.append("Type:\"Text\", ");
            				editGbn = false;
            			}
            			
            		}
            		if("id".equalsIgnoreCase(patternStr)) {
            			ibsheetStr.append("SaveName:\""+mapstr.get(patternStr) +"\", ");
            		}
            		if("width".equalsIgnoreCase(patternStr)) {					//width 값이 0인경우 컬럼 hidden 처리
            			if(mapstr.get(patternStr) != null) {
            				try {
            					if(Integer.parseInt(mapstr.get(patternStr)) == 0) {
            						ibsheetStr.append("Hidden:true, ");
            					}else {
            						ibsheetStr.append("MinWidth:" + mapstr.get(patternStr) + ", ");            					
            					}
							} catch (Exception e) {
								if(!"*".equals(mapstr.get(patternStr))) {
									System.out.println("width 알수 없음");
									ibsheetStr.append("MinWidth:" + mapstr.get(patternStr) + ", ");
								}
							}
            			}
            			if("*".equals(mapstr.get(patternStr))) {
            				ibsheetStr.append("MinWidth:\"*\", ");
            			}
            		}
            		if("align".equalsIgnoreCase(patternStr)) {
            			if(mapstr.get(patternStr) != null) {
            				String alignstr = stringFirstUpper(mapstr.get(patternStr));
            				ibsheetStr.append("Align:\""+ alignstr +"\", ");
            			}
            		}
            		if("required".equalsIgnoreCase(patternStr)) {
            			if(mapstr.get(patternStr) != null && mapstr.get(patternStr).equals("true")) {
            				ibsheetStr.append("KeyField:1, ");
            			}
            		}
            		if("maxLength".equalsIgnoreCase(patternStr)) {
            			if(mapstr.get(patternStr) != null && mapstr.get(patternStr).equals("true")) {
            				ibsheetStr.append(" EditLen:" + mapstr.get(patternStr) + ", ");
            			}
            		}
            	}
            	
            	if(ibsheetStr.toString().toLowerCase().indexOf("align") < 0) {				//정렬이 없는경우 기본값은 가운데 정렬
        			ibsheetStr.append("Align:\"center\", ");
        		}
            	
        		ibsheetStr.append("Edit:" + editGbn);
            	
            	ibsheetArr.add(ibsheetStr.toString() +" },");
            }
            for(String ibsheet : ibsheetArr) {
            	System.out.println(ibsheet);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(br != null) try {br.close(); } catch (IOException e) {}
        }

	}


	public static String stringFirstUpper(String data) {
		String transString = data.substring(0,1);
		transString = transString.toUpperCase();
		transString = transString +  data.substring(1);
		return transString;
	}
}
