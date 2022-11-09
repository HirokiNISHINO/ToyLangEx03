package kut.compiler.parser;

import java.io.IOException;

import kut.compiler.exception.CompileErrorException;
import kut.compiler.exception.SyntaxErrorException;
import kut.compiler.lexer.Lexer;
import kut.compiler.lexer.Token;
import kut.compiler.lexer.TokenClass;
import kut.compiler.parser.ast.AstNode;
import kut.compiler.parser.ast.AstBinOp;
import kut.compiler.parser.ast.AstIntLiteral;
import kut.compiler.parser.ast.AstProgram;

/**
 * @author hnishino
 *
 */
public class Parser 
{
	protected Lexer 	lexer;
	protected Token		currentToken;
	
	/**
	 * @param lexer
	 */
	public Parser(Lexer lexer) {
		this.lexer = lexer;
	}
	
	
	
	/**
	 * @return
	 */
	protected Token getCurrentToken()
	{
		return this.currentToken;
	}
	
	/**
	 * 
	 */
	/**
	 * 
	 */
	protected void consumeCurrentToken() throws IOException, CompileErrorException
	{
		this.currentToken = lexer.getNextToken();
		return;
	}
	
	/**
	 * @return
	 */
	public AstNode parse() throws IOException, CompileErrorException
	{
		//read the first token.
		consumeCurrentToken();
		AstNode program = program();
		
		Token t = this.getCurrentToken();
		if (t.getC() != TokenClass.EOF) {
			throw new SyntaxErrorException("the program is not correctly terminated.");
		}
		
		return program;
	}
	 

	/**
	 * @return
	 */
	protected AstNode program() throws IOException, CompileErrorException
	{
		AstNode node = additiveExp();
		return new AstProgram(node);
	}
	
	/**
	 * @return
	 * @throws IOException
	 * @throws CompileErrorException
	 */
	public AstNode additiveExp() throws IOException, CompileErrorException
	{
		AstNode lhs = multiplicativeExpr();
		while(true) {
			Token t = this.getCurrentToken();
			
			if (t == null) {
				break;
			}
			
			if (t.getC() != '+' && t.getC() != '-') {
				break;
			}
			this.consumeCurrentToken();
			
			AstNode rhs = multiplicativeExpr();
			AstNode binop = new AstBinOp(lhs, rhs, t);
			lhs = binop;
		}
		return lhs;

	}
	
	/**
	 * @return
	 * @throws IOException
	 * @throws CompileErrorException
	 */
	public AstNode multiplicativeExpr() throws IOException, CompileErrorException
	{
		AstNode lhs = integer();
		
		while(true) {
			Token t = this.getCurrentToken();
			
			if (t == null) {
				break;
			}
			
			if (t.getC() != '*' && t.getC() != '/') {
				break;
			}
			this.consumeCurrentToken();
			
			AstNode rhs = multiplicativeExpr();
			AstNode binop = new AstBinOp(lhs, rhs, t);
			lhs = binop;
		}
		return lhs;
	}
	

	 
	
	/**
	 * @return
	 * @throws IOException
	 * @throws CompileErrorException
	 */
	protected AstNode integer() throws IOException, CompileErrorException
	{
		Token t = this.getCurrentToken();
		if (t.getC() != TokenClass.IntLiteral) {
			throw new SyntaxErrorException("expected an integer literal, but found: " + t.getL());
		}
		
		AstNode node = new AstIntLiteral(t);
		this.consumeCurrentToken();
		
		return node;
	}
	
	
}
