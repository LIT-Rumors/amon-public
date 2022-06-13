













### Grammar

[XText DSL File](xtext.dsl)


```Model: 	namespace=Namespace contracts+=Assumption* defaults+=DefaultValues* prules+=MonitoringRule*  grules+=GlobalRule* constraints+=ConstraintRule*;```

``Namespace:	'namespace' namespace=STRING ';';``


``Assumption: 	'Assumption' name=STRING frequency=MinFrequency  ';';``

``DefaultValues:	'Default' name=STRING value=State ';';``

`` State: 	state=(OFF|KEEP);``



``MinFrequency:	'MIN_PERIOD:' frequency=DOUBLE 'seconds';``

``ConstraintRule:	'Constraint' name=STRING scope=Scope  'requiresmonitor' monitors+=STRING* ';';``

``Applicability:	'applies' trigger=(ApplicationID | ApplicationGlobal | ApplicationGroup);``

``ApplicationID:	'for' 'device' '[' event=STRING ']';``

``ApplicationGroup:	'for' 'group' '(' group=STRING ')';``

``ApplicationGlobal:	sc='forall';``

``Scope:	'scope' scope=('local' | 'central');``

``MonitoringRule:	'Rule' 'PREPLANNED' name=STRING applicability=Applicability  trigger=Trigger monitors+=Monitor* ';';``
	
``GlobalRule:	'Rule' 'UBIQUITOUS' name=STRING applicability=Applicability ('salience' salience=INT)?  trigger=Trigger monitors+=Monitor*  ';';``
	

``Trigger:	'Trigger' trigger=(Event | Context | Data);``

``Context:	'Context' '[' context=STRING ']' status?=Status?;``

``Status:	'ENTRY' | 'EXIT';``

``Event:	'Event' '[' event=STRING ']' ('and' 'Event' '[' ontop+=STRING ']')* ;``

``Data:	'Data' '[' value=STRING 'when' condition=ValueCondition ']';``


``Monitor:	'Monitor' name=STRING ':' changes+=ChangeType*;``

``ChangeType:	ChangeFrequency | ChangeState | ChangeScope | ChangeReportingFrequency;``

``ChangeScope:	'CHANGE_SCOPE:' scope=Scope;``

``ChangeState:	'CHANGE_STATE:' scope=(ON | OFF);``

``ChangeFrequency:	'CHANGE_PERIOD:' 'every' frequency=DOUBLE 'seconds';``

``ChangeReportingFrequency:	'CHANGE_REPORTING_PERIOD:' 'every' frequency=DOUBLE 'seconds';``
